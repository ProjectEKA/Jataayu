package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.NetworkManager
import `in`.projecteka.jataayu.network.interceptor.NoConnectivityException
import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.presentation.ui.activity.NoInternetConnectionActivity
import okhttp3.ResponseBody
import org.koin.core.context.GlobalContext.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

fun <T> PayloadLiveData<T>.success(data: T?) {
    loading(false)
    value = Success(data)
}

fun <T> PayloadLiveData<T>.loading(isLoading: Boolean, message: String? = null) {
    value = Loading(isLoading, message)
}

fun <T> PayloadLiveData<T>.failure(error: Throwable) {
    loading(false)
    value = Failure(error)
}

fun <T> PayloadLiveData<T>.partialFailure(error: Error?) {
    loading(false)
    value = PartialFailure(error)
}

fun <T> PayloadLiveData<T>.isLoading(): Boolean {
    val currentValue = value
    return if (currentValue is Loading) {
        currentValue.isLoading
    } else false
}


fun <T> PayloadLiveData<T>.fetch(call: Call<T>): PayloadLiveData<T> {

   if (!NetworkManager.hasInternetConnection()) {
       NoInternetConnectionActivity.start(NetworkManager.context) {
            fetch(call)
       }
       return this
   }

    call.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            if (t is NoConnectivityException) {
                loading(false)
                NoInternetConnectionActivity.start(NetworkManager.context) {
                    fetch(call.clone())
                }
            } else {
                failure(t)
            }
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {

            if (response.isSuccessful) {
                success(response.body())
            } else {
                try {
                    response.errorBody()?.let {
                        if (it.contentType()?.type == "application") {
                            val errorConverter: Converter<ResponseBody, ErrorResponse> =
                                get().koin.get()
                            partialFailure(errorConverter.convert(it)?.error)
                        } else {
                            failure(Exception("Something went wrong"))
                        }
                    } ?: failure(Exception("Unknown Error"))
                } catch (e: java.lang.Exception){
                    failure(e)
                }
            }
        }
    }).also {
        loading(true)
    }
    return this
}
