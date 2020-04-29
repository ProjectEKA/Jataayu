package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.utils.PayloadLiveData
import `in`.projecteka.jataayu.network.utils.fetch
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.registration.model.LoginRequest
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.registration.ui.fragment.LoginFragment
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

class LoginViewModel(private val repository: AuthenticationRepository,
                     val preferenceRepository: PreferenceRepository,
                     val credentialsRepository: CredentialsRepository): ViewModel(), ItemClickCallback {

    companion object {
        const val GRANT_TYPE_PASSWORD = "password"
    }

    var loginResponse = PayloadLiveData<CreateAccountResponse>()

    fun login(userName: String, password: String){
        loginResponse.fetch(repository.login(LoginRequest(userName, password, GRANT_TYPE_PASSWORD)))
    }

    fun getAuthTokenWithTokenType(authToken: String?, tokenType: String?): String {
        return (tokenType?.capitalize()+ LoginFragment.SPACE + authToken)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {

    }
}