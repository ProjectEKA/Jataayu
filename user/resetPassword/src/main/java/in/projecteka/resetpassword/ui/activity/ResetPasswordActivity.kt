package `in`.projecteka.resetpassword.ui.activity

import `in`.projecteka.forgotpassword.R
import `in`.projecteka.forgotpassword.databinding.ActivityResetPasswordBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.resetpassword.ui.fragment.ReadIdentifierFragment
import `in`.projecteka.resetpassword.ui.fragment.ResetPasswordFragment
import `in`.projecteka.resetpassword.ui.fragment.ResetPasswordOtpFragment
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordActivityViewModel
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>() {

    private val viewmodel: ResetPasswordActivityViewModel by viewModel()

    companion object {
       const val snackbarMargin = 16
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        viewmodel.init()
    }

    private fun initObservers() {
        viewmodel.redirectTo.observe(this, Observer {
            addFragment(when(it){
                ResetPasswordActivityViewModel.Show.FIRST_SCREEN ->
                    ReadIdentifierFragment.newInstance()
                ResetPasswordActivityViewModel.Show.SECOND_SCREEN ->
                    ResetPasswordOtpFragment.newInstance()
                ResetPasswordActivityViewModel.Show.THIRD_SECREEN ->
                    ResetPasswordFragment.newInstance()

            },R.id.fragment_container)
        })
    }

    override fun layoutId(): Int =
        R.layout.activity_reset_password
}
