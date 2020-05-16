package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityChangePasswordBinding
import `in`.projecteka.jataayu.user.account.viewmodel.ChangePasswordViewModel
import `in`.projecteka.jataayu.util.extension.showLongToast
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {

    private val viewModel: ChangePasswordViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_change_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initObservers()
    }

    private fun initBindings() {
        binding.viewModel = viewModel
    }

    private fun initObservers() {
        binding.etOldPassword.addTextChangedListener { text ->
            viewModel.validateOldPassword()
        }
        binding.etCreatePassword.addTextChangedListener { text ->
            viewModel.validatePassword()
        }
        binding.etConfirmPassword.addTextChangedListener { text ->
            viewModel.validatePassword()
        }

        viewModel.changePasswordResponse.observe(this, Observer {
            when (it) {
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
                    showLongToast(getString(R.string.password_changed))
                    finish()
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
