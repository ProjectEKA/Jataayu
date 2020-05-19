package `in`.projecteka.jataayu.registration.viewmodel

import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.model.APIResponse
import `in`.projecteka.jataayu.network.model.Error
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.registration.repository.AuthenticationRepository
import `in`.projecteka.jataayu.registration.ui.activity.LoginActivity
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.CredentialsRepository
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.content.res.Resources
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

class PasswordInputViewModel(
    private val credentialsRepository: CredentialsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val repository: AuthenticationRepository
) : BaseViewModel(), TextWatcher {

    val accountLockBlockEnable = ObservableField<Int>(View.GONE)
    val accountLockBlockDividerEnable = ObservableField<Int>(View.GONE)
    val passwordInputType = ObservableInt(hiddenPasswordInputType)
    val loginEnabled = ObservableBoolean(false)

    val inputPasswordLbl = ObservableField<String>()
    val inputPasswordVisibilityToggleLbl = ObservableField<Int>(R.string.show)
    val onPasswordVisibilityToggleEvent = SingleLiveEvent<Int>()
    val onClickForgotPasswordEvent = SingleLiveEvent<Void>()
    val errorDialogEvent = SingleLiveEvent<String>()

    private val loginByPasswordResponse = PayloadLiveData<CreateAccountResponse>()
    val createAccountResponse: LiveData<APIResponse<out CreateAccountResponse>?> =
        Transformations.map(loginByPasswordResponse) { response ->
            when (response) {
                is Success -> {
                    APIResponse(response.data, null)
                }
                is PartialFailure -> {
                    APIResponse(null, response.error)
                }
                is Failure -> {
                    APIResponse(null, APIResponse.getError(response.error))
                }
                else -> {
                    null
                }
            }
        }


    private val visiblePasswordInputType: Int
        get() = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    private val hiddenPasswordInputType: Int
        get() = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        loginEnabled.set(inputPasswordLbl.get()?.isNotEmpty() == true)
    }

    fun onForgotPasswordClicked() {
        onClickForgotPasswordEvent.call()
    }

    fun togglePasswordVisible() {
        val newInputType = when (passwordInputType.get()) {
            visiblePasswordInputType -> {
                inputPasswordVisibilityToggleLbl.set(R.string.show)
                hiddenPasswordInputType
            }
            hiddenPasswordInputType -> {
                inputPasswordVisibilityToggleLbl.set(R.string.hide)
                visiblePasswordInputType
            }
            else -> hiddenPasswordInputType
        }
        passwordInputType.set(newInputType)
        onPasswordVisibilityToggleEvent.value = inputPasswordLbl.get()?.length ?: 0
    }

    fun onLoginClicked(cmId: String) {

        showProgress(true, R.string.logging_in)
        val call = repository.login(
            cmId,
            inputPasswordLbl.get()!!, LoginViewModel.GRANT_TYPE_PASSWORD
        )
        loginByPasswordResponse.fetch(call)
    }

    fun onLoginSuccess(response: CreateAccountResponse) {
        credentialsRepository.accessToken =
            "${response.tokenType.capitalize()} ${response.accessToken}"
        preferenceRepository.isUserLoggedIn = true
        credentialsRepository.refreshToken = response.refreshToken
    }

    fun onLoginFailure(error: Error?, resources: Resources) {

        error?.let {
            when (it.code) {
                LoginActivity.ERROR_CODE_BLOCK_USER -> {
                    accountLockBlockEnable.set(View.VISIBLE)
                    accountLockBlockDividerEnable.set(View.VISIBLE)
                }
                else -> {
                    if (error.message.isNotEmpty()) {
                        errorDialogEvent.value = error.message
                    } else {
                        errorDialogEvent.value = resources.getString(R.string.something_went_wrong)
                    }
                }
            }
        } ?: kotlin.run {
            errorDialogEvent.value = resources.getString(R.string.something_went_wrong)
        }
    }
}


