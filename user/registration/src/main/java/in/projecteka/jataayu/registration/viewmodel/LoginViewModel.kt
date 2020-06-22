package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class LoginViewModel : BaseViewModel() {

    companion object {
        const val GRANT_TYPE_PASSWORD = "password"
    }

    var cmId: String = ""
    private set


    private val currentFragmentLayout = MutableLiveData(R.layout.consent_manager_id_input_fragment)
    val redirectLiveEvent : LiveData<Int> = Transformations.map(currentFragmentLayout) {
        it
    }

    val loginResponseSuccessEvent = SingleLiveEvent<Void>()

    fun replaceFragment(@LayoutRes layoutRes: Int) {
        currentFragmentLayout.value = layoutRes
    }

    fun updateConsentManagerID(id: String, provider: String) {
        cmId = id + provider
    }
}