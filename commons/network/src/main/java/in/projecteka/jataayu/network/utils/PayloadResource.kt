package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.NetworkManager
import `in`.projecteka.jataayu.network.model.Error
import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject


typealias PayloadLiveData<T> = ContextLiveData<PayloadResource<T>>

sealed class PayloadResource<T>

data class Success<T>(val data: T?) : PayloadResource<T>()

data class Failure<T>(val error: Throwable) : PayloadResource<T>()

data class PartialFailure<T>(val error: Error?) : PayloadResource<T>()

data class Loading<T>(val isLoading: Boolean, val message: String? = null) : PayloadResource<T>()


class ContextLiveData<T>: MutableLiveData<T>(), KoinComponent {

    val networkManager: NetworkManager? by inject()
}