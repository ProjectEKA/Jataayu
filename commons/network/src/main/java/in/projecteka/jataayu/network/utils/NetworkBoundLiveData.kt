package `in`.projecteka.jataayu.network.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class NetworkBoundLiveData<T>: MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        registerNetworkChangeEvent()
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(observer)
        registerNetworkChangeEvent()
    }

    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(observer)
        unRegisterNetworkChangeEvent()
    }

    private fun registerNetworkChangeEvent() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    private fun unRegisterNetworkChangeEvent() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    // This method will be called when a network status is posted
    @Subscribe
    fun updateNetworkStatus(networkStatus: NetworkStatus) {
        internetStatus = networkStatus
    }

    fun canFetch(): Boolean {
        return internetStatus.hasInternetConnection
    }
}

data class NetworkStatus(val hasInternetConnection: Boolean)

// create once and update internet status.
// the internet status is helpful if user turn off the internet switch between pager or tab.
// API call and offline screen can be shown
private var internetStatus = NetworkStatus(true)