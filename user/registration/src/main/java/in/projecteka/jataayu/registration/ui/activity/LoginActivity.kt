package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.registration.ui.activity.databinding.ActivityLoginBinding
import `in`.projecteka.jataayu.registration.ui.fragment.ConsentManagerIDInputFragment
import `in`.projecteka.jataayu.registration.ui.fragment.LoginOtpFragment
import `in`.projecteka.jataayu.registration.ui.fragment.PasswordInputFragment
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.startDashboard
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    val viewModel: LoginViewModel by viewModel()
    companion object {
        const val ERROR_CODE_BLOCK_USER = 1031
    }
    override fun layoutId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        addFragment(ConsentManagerIDInputFragment.newInstance(), R.id.fragment_container)
    }


    private fun initObservers() {

        viewModel.redirectLiveEvent.observe(this, Observer {
            when(it) {
                R.layout.password_input_fragment -> addFragment(PasswordInputFragment.newInstance(), R.id.fragment_container)
                R.layout.login_otp_fragment -> addFragment(LoginOtpFragment.newInstance(), R.id.fragment_container)
            }
        })

        viewModel.loginResponseSuccessEvent.observe(this, Observer {
            finish()
            startDashboard(this) {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        })
    }
}