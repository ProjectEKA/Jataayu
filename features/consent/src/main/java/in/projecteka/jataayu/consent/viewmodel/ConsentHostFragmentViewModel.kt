package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ConsentHostFragmentViewModel() : ViewModel(), SwipeRefreshLayout.OnRefreshListener {

    //refreshDataEvent
    val pullToRefreshEvent = MutableLiveData<Boolean>()
    val isRefreshing = ObservableBoolean(false)

    internal enum class Action {
        SELECT_CONSENTS_TAB
    }

    companion object {
        const val REQUEST_CONSENT_DETAILS = 500
        const val RESULT_CONSENT_GRANTED = 501
        const val RESULT_DENY_CONSENT = 601
    }

    internal val viewPagerState = SingleLiveEvent<Action>()

    fun setUp(){
        viewPagerState.value = Action.SELECT_CONSENTS_TAB
    }

    override fun onRefresh() {
        isRefreshing.set(true)
        pullToRefreshEvent.value = true
    }

    fun showRefreshing(isRefresh: Boolean){
        isRefreshing.set(isRefresh)
    }

    fun selectConsentsTab(){
        viewPagerState.value = Action.SELECT_CONSENTS_TAB
    }
}