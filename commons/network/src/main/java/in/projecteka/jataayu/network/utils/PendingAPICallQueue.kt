package `in`.projecteka.jataayu.network.utils

import retrofit2.Call


class PendingAPICallQueue {

    companion object {
        private val pendingAPICallMap: MutableMap<Any, Any> = mutableMapOf()
        private val pendingAPICallList: ArrayList<Any> = arrayListOf()
    }

    val hasPendingAPICall: Boolean
    get() = pendingAPICallList.isNotEmpty() || pendingAPICallMap.isNotEmpty()

    fun <T> add(liveData: PayloadLiveData<T>, call: Call<T>) {
        pendingAPICallMap[liveData] = call
    }

    fun <T> add(call: Call<T>) {
        pendingAPICallList.add(call)
    }

    fun <T> execute() {
        pendingAPICallMap.map {
            (it.key as PayloadLiveData<T>).fetch(it.value as Call<T>)
        }
        clearQueue()
    }

    fun clearQueue() {
        pendingAPICallMap.clear()
        pendingAPICallList.clear()
    }
}
