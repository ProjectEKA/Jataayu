package `in`.projecteka.jataayu.presentation.ui.viewmodel

import androidx.lifecycle.MutableLiveData

class ProviderAddedLiveData {
    companion object {
        var providerAddedEvent = MutableLiveData<Boolean>(false)
    }
}