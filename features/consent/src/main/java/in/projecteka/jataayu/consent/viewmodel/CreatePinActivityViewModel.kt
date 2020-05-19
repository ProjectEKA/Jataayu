package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.core.ConsentScopeType
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class CreatePinActivityViewModel() : ViewModel() {
    companion object {
        const val KEY_SCOPE_TYPE = "scope_type"
    }
    var scopeType = ObservableField<ConsentScopeType>(ConsentScopeType.SCOPE_GRAND)
}

