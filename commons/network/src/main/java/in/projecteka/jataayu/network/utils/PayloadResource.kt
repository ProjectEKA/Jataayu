package `in`.projecteka.jataayu.network.utils

import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody


typealias PayloadLiveData<T> = MutableLiveData<PayloadResource<T>>

sealed class PayloadResource<T>

data class Success<T>(val data: T?) : PayloadResource<T>()

data class Failure<T>(val error: Throwable) : PayloadResource<T>()

data class PartialFailure<T>(val responseBody: ResponseBody?) : PayloadResource<T>()

data class Loading<T>(val isLoading: Boolean, val message: String? = null) : PayloadResource<T>()