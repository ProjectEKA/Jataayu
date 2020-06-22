package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.network.model.ErrorResponse
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

abstract class RetrofitCallback<T> : Callback<T?> {
    private var responseCallback: ResponseCallback? = null
    private val DEFAULT_ERROR_CODE = -1
    private val ERROR_CODE_UNAUTHORIZED = 1017




    constructor(responseCallback: ResponseCallback?) {
        this.responseCallback = responseCallback
    }

    protected abstract fun observableLiveData(): MutableLiveData<T>
    override fun onResponse(call: Call<T?>, response: Response<T?>) {
        if (response.isSuccessful) {
            responseCallback?.onSuccess(response.body())
            if (response.body() != null) {
                observableLiveData().value = response.body()
            }
        } else {
            if (response.errorBody() != null) {
                try {
                    val errorResponse: ErrorResponse = Gson()
                        .fromJson<ErrorResponse>(
                            response.errorBody()!!.string(),
                            ErrorResponse::class.java
                        )
                    responseCallback?.onFailure(errorResponse)
                } catch (e: Exception) {
                    e.printStackTrace()
                    var errorCode = DEFAULT_ERROR_CODE
                    if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        errorCode = ERROR_CODE_UNAUTHORIZED
                    }
                    responseCallback?.onFailure(
                        ErrorResponse(
                            Error(
                                errorCode,
                                e.message!!
                            )
                        )
                    )
                }
            }
        }
    }

    override fun onFailure(call: Call<T?>, t: Throwable) {
         responseCallback?.onFailure(t)
    }


}