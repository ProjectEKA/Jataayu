package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.registration.ui.activity.databinding.RegistrationActivityBinding
import `in`.projecteka.jataayu.registration.ui.fragment.RegistrationFragment
import `in`.projecteka.jataayu.registration.ui.fragment.RegistrationOtpFragment
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel.Show
import `in`.projecteka.jataayu.util.startAccountCreation
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : BaseActivity<RegistrationActivityBinding>() {

    override fun layoutId(): Int = R.layout.registration_activity

    private val viewModel by viewModel<RegistrationActivityViewModel>()

    private var shouldClearBackStack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        viewModel.redirectToRegistrationScreen()
        initObservers()
        initToolbar()
    }

    override fun onStop() {
        if(shouldClearBackStack) {
            // clear back stack without animation.
            // mobile number fragment should display when user press the back from account creation
            val backStackEntry = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)
            supportFragmentManager.popBackStack(backStackEntry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        super.onStop()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun initObservers() {
        viewModel.redirectTo.observe(this, Observer {
            when (it) {
                Show.VERIFICATION -> {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if(currentFragment is RegistrationFragment)
                        replaceFragment(RegistrationOtpFragment.newInstance(), R.id.fragment_container)
                }
                Show.REGISTRATION -> {
                    replaceFragment(RegistrationFragment.newInstance(), R.id.fragment_container)
                }
                Show.NEXT -> {
                    shouldClearBackStack = true
                    startAccountCreation(this)
                }
            }
        })

        viewModel.requestVerificationResponseLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    viewModel.redirectToOtpScreen(it.data?.sessionId)
                }
                is Loading -> {
                    viewModel.showProgress(it.isLoading, R.string.sending_otp)
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
                is PartialFailure -> {
                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (currentFragment is RegistrationFragment) {
                        var message = it.error?.message
                        var title = getString(R.string.failure)
                        if(it.error?.code == RegistrationOtpFragment.ERROR_CODE_OTP_LIMIT_EXCEEDED){
                            message = getString(R.string.otp_limit_exceeded)
                            title = getString(R.string.request_limit_exceeded)
                        }
                        showAlertDialog(title, message, getString(android.R.string.ok))
                    }
                }
            }
        })

        viewModel.verifyIdentifierResponseLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    viewModel.credentialsRepository.accessToken = it.data?.temporaryToken
                    viewModel.preferenceRepository.isUserRegistered = true
                    viewModel.preferenceRepository.mobileIdentifier = viewModel.getIdentifierValue()
                    viewModel.redirectToNext()
                }
                is Loading -> {
                    viewModel.showProgress(it.isLoading, R.string.sending_otp)
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }
}