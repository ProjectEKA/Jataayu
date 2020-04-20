package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ConsentHostFragmentViewModel() : ViewModel(), SwipeRefreshLayout.OnRefreshListener {

    //refreshDataEvent
    val refresh = MutableLiveData<Boolean>()
    val isRefreshing = ObservableBoolean(false)

    internal enum class Action {
        ADD_FRAGMENTS
    }

    internal val viewPagerState = SingleLiveEvent<Action>()

    fun setUp(){
        viewPagerState.value = Action.ADD_FRAGMENTS
    }

    override fun onRefresh() {
        isRefreshing.set(true)
        refresh.value = true
    }

    fun setIsRefreshing(isRefresh: Boolean){
        isRefreshing.set(isRefresh)
    }
}