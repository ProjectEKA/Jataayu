package `in`.projecteka.jataayu.network.utils

import retrofit2.Call
import retrofit2.Callback
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
                failure(RuntimeException(response.errorBody().toString()))
            }
        }
    })
}