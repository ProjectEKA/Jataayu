package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.network.model.ErrorResponse
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

fun <T> PayloadLiveData<T>.fetch(call: Call<T>) {
    value = Loading(true)
    call.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            failure(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                success(response.body())
            } else {
                response.errorBody()?.let{
                    if(it.contentType()?.type == "text") {
                        val errorConverter: Converter<ResponseBody, ErrorResponse> =
                            get().koin.get()
                        partialFailure(errorConverter.convert(it)?.error)
                    }else{
                        failure(Exception("Something went wrong"))
                    }
                } ?: failure(Exception("Unknown Error"))

            }
        }
    })
}