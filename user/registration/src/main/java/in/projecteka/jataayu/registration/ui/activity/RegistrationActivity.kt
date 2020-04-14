package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.registration.ui.fragment.RegistrationFragment
import `in`.projecteka.jataayu.registration.ui.fragment.RegistrationOtpFragment
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel.Show
import `in`.projecteka.jataayu.util.sharedPref.setAuthToken
import `in`.projecteka.jataayu.util.sharedPref.setIsUserRegistered
import `in`.projecteka.jataayu.util.startAccountCreation
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : BaseActivity() {

    private val viewModel by viewModel<RegistrationActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.redirectToRegistrationScreen()
        initObservers()
    }

    private fun initObservers() {
        viewModel.redirectTo.observe(this, Observer {
            when (it) {
                Show.VERIFICATION -> replaceFragment(RegistrationOtpFragment.newInstance())
                Show.REGISTRATION -> replaceFragment(RegistrationFragment.newInstance())
                Show.NEXT -> {
                    finish()
                    startAccountCreation(this) {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                }
            }
        })

        viewModel.requestVerificationResponseLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    viewModel.redirectToOtpScreen(it.data?.sessionId)
                }
                is Loading -> {
                    showProgressBar(it.isLoading, getString(R.string.sending_otp))
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
                is PartialFailure -> {
                    showAlertDialog(getString(R.string.failure), it.error?.message, getString(android.R.string.ok))
                }
            }
        })
        viewModel.verifyIdentifierResponseLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    it.data?.temporaryToken?.let { token ->
                        setAuthToken(token)
                        setIsUserRegistered(true)
                    }
                    viewModel.redirectToNext()
                }
                is Loading -> {
                    showProgressBar(it.isLoading, getString(R.string.sending_otp))
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }
}