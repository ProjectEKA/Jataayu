package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
import androidx.lifecycle.MutableLiveData

class ConsentDetailsActivityViewModel() : BaseViewModel() {
    var grantConsentAfterProviderLinkEvent = MutableLiveData<Boolean>(false)
}

