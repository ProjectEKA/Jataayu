package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.network.model.ErrorResponse
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

private const val DEFAULT_ERROR_CODE = -1
private const val ERROR_CODE_UNAUTHORIZED = 1017

abstract class RetrofitCallback<T> : Callback<T?> {
    private var responseCallback: ResponseCallback? = null

    constructor()

    constructor(responseCallback: ResponseCallback?) {
        this.responseCallback = responseCallback
    }

    protected abstract fun observableLiveData(): MutableLiveData<T?>
    override fun onResponse(call: Call<T?>, response: Response<T?>) {
        if (response.isSuccessful) {
            if (responseCallback != null) responseCallback!!.onSuccess(response.body())
            if (response.body() != null) {
                observableLiveData().value = response.body()
            }
        } else {
            if (response.errorBody() != null && responseCallback != null) {
                try {
                    val errorResponse: ErrorResponse = Gson()
                        .fromJson<ErrorResponse>(response.errorBody()!!.string(), ErrorResponse::class.java)
                    responseCallback!!.onFailure(errorResponse)
                } catch (e: Exception) {
                    e.printStackTrace()
                    var errorCode = DEFAULT_ERROR_CODE
                    if (response.code() in arrayOf(HTTP_FORBIDDEN, HTTP_UNAUTHORIZED)) {
                        errorCode = ERROR_CODE_UNAUTHORIZED
                    }
                    responseCallback!!.onFailure(ErrorResponse(Error(errorCode, e.message!!)))
                }
            }
        }
    }

    override fun onFailure(call: Call<T?>, t: Throwable) {
        if (responseCallback != null) responseCallback!!.onFailure(t)
    }
}