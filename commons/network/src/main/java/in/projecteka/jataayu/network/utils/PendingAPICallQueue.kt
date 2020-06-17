package `in`.projecteka.jataayu.network.utils

import retrofit2.Call


class PendingAPICallQueue {

    companion object {
        private val pendingAPICallMap: MutableMap<Any, Any> = mutableMapOf()
    }

    val hasPendingAPICall: Boolean
    get() = pendingAPICallMap.isNotEmpty()

    fun <T> add(liveData: PayloadLiveData<T>, call: Call<T>) {
        pendingAPICallMap[liveData] = call
    }

    fun <T> execute() {
        pendingAPICallMap.map {
            (it.key as PayloadLiveData<T>).fetch(it.value as Call<T>)
        }
        clearQueue()
    }

    fun clearQueue() {
        pendingAPICallMap.clear()
    }
}
