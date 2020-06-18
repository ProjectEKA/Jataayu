package `in`.projecteka.jataayu.provider.viewmodel

import `in`.projecteka.jataayu.core.ProviderLinkType
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProviderActivityViewModel() : ViewModel() {
    val showSnackbarevent = MutableLiveData<Boolean>(false)
    val providerLinkType = ObservableField<ProviderLinkType>(ProviderLinkType.LINK_NORMAL)
}

