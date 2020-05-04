package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ConsentHostFragmentViewModel() : ViewModel(), SwipeRefreshLayout.OnRefreshListener {

    val pullToRefreshEvent = MutableLiveData<Boolean>()
    val showToastEvent = MutableLiveData<String>()
    val isRefreshing = ObservableBoolean(false)

    internal enum class Action {
        SELECT_CONSENTS_TAB
    }

    internal val redirectEvent = SingleLiveEvent<Action>()
    companion object {
        const val REQUEST_CONSENT_DETAILS = 500
        const val KEY_CONSENT_EVENT_TYPE = "consent_event_type"
        const val KEY_EVENT_GRANT = "grant"
        const val KEY_EVENT_DENY = "deny"
    }

    internal val viewPagerState = SingleLiveEvent<Action>()

    fun setUp(){
        pullToRefreshEvent.value = false
        selectConsentsTab()
    }

    override fun onRefresh() {
        isRefreshing.set(true)
        pullToRefreshEvent.value = true
    }

    fun showRefreshing(isRefresh: Boolean){
        isRefreshing.set(isRefresh)
    }

    fun selectConsentsTab(){
        redirectEvent.value = Action.SELECT_CONSENTS_TAB
    }
}