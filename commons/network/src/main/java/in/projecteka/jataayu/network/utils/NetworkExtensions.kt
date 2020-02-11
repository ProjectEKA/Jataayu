package `in`.projecteka.jataayu.network.utils

import androidx.lifecycle.MutableLiveData
import retrofit2.Call

fun <T> Call<T>.observeOn(mutableLiveData: MutableLiveData<T>, responseCallback: ResponseCallback) {
    enqueue(object: RetrofitCallback<T>(responseCallback) {
        override fun observableLiveData(): MutableLiveData<T> {
            return mutableLiveData
        }
    })
}

fun <T> Call<T>.observeOn(mutableLiveData: MutableLiveData<T>) {
    enqueue(object: RetrofitCallback<T>() {
        override fun observableLiveData(): MutableLiveData<T> {
            return mutableLiveData
        }
    })
}