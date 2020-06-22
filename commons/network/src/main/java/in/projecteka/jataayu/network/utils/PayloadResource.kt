package `in`.projecteka.jataayu.network.utils

import `in`.projecteka.jataayu.network.model.Error
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


typealias PayloadLiveData<T> = NetworkBoundLiveData<PayloadResource<T>>

sealed class PayloadResource<T>

data class Success<T>(val data: T?) : PayloadResource<T>()

data class Failure<T>(val error: Throwable) : PayloadResource<T>()

data class PartialFailure<T>(val error: Error?) : PayloadResource<T>()

data class Loading<T>(val isLoading: Boolean, val message: String? = null) : PayloadResource<T>()

