package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.registration.ui.activity.databinding.ActivityLoginBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.sharedPref.setAuthToken
import `in`.projecteka.jataayu.util.sharedPref.setIsUserLoggedIn
import `in`.projecteka.jataayu.util.startDashboard
import `in`.projecteka.jataayu.util.startRegistration
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    val viewModel: LoginViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        initObservers()
    }

    private fun initObservers() {
        viewModel.onClickRegisterEvent.observe(this, Observer {
            startRegistration(this)
        })

        viewModel.onPasswordVisibilityToggleEvent.observe(this, Observer {
            binding.etPassword.setSelection(it)
        })

        viewModel.loginResponse.observe(this, Observer {

            when (it) {
                is Loading -> viewModel.showProgress(it.isLoading, R.string.logging_in)
                is Success -> {
                    setAuthToken(viewModel.getAuthTokenWithTokenType(authToken = it.data?.accessToken, tokenType = it.data?.tokenType))
                    setIsUserLoggedIn(true)
                    finish()
                    startDashboard(this)

                }
                is PartialFailure -> {
                    showAlertDialog(
                        getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok)
                    )
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }
}