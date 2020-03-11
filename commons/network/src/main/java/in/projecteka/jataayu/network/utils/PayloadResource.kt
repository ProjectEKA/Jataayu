package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.model.Error
import androidx.lifecycle.MutableLiveData


typealias PayloadLiveData<T> = MutableLiveData<PayloadResource<T>>

sealed class PayloadResource<T>

data class Success<T>(val data: T?) : PayloadResource<T>()

data class Failure<T>(val error: Throwable) : PayloadResource<T>()

data class PartialFailure<T>(val error: Error?) : PayloadResource<T>()

data class Loading<T>(val isLoading: Boolean, val message: String? = null) : PayloadResource<T>()