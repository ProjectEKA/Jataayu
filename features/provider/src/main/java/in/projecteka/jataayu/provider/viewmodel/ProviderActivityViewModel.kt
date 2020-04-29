package `in`.projecteka.jataayu.provider.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProviderActivityViewModel() : ViewModel() {
    val showSnackbarevent = MutableLiveData<Boolean>(false)
}

