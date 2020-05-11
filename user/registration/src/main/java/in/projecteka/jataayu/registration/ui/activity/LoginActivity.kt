package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.wobble
import `in`.projecteka.jataayu.registration.ui.activity.databinding.ActivityLoginBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.startDashboard
import `in`.projecteka.jataayu.util.startRegistration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    val viewModel: LoginViewModel by viewModel()
    companion object {
        private const val ERROR_CODE_BLOCK_USER = 1031
    }
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
                    finish()
                    viewModel.credentialsRepository.accessToken = "${it.data?.tokenType?.capitalize()} ${it.data?.accessToken}"
                    viewModel.preferenceRepository.isUserLoggedIn = true
                    viewModel.credentialsRepository.refreshToken =  it.data?.refreshToken
                    startDashboard(this)

                }
                is PartialFailure -> {
                    when (it.error?.code) {
                        ERROR_CODE_BLOCK_USER -> viewModel.accountLockBlockEnable.set(View.VISIBLE)
                        else ->{
                            showAlertDialog(
                                getString(R.string.failure), it.error?.message,
                                getString(android.R.string.ok)
                            )}
                    }

                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }
}