package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent

class NoMatchingRecordsFragmentViewModel() : BaseViewModel() {

    val redirectToReviewEvent = SingleLiveEvent<Void>()

    fun onReview(){
        redirectToReviewEvent.call()
    }
}