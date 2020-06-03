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
import `in`.projecteka.jataayu.util.startLogin
import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {

    companion object {
        private const val ERROR_CODE_INVALID_OLD_PASSWORD = 1018
        private const val ERROR_CODE_NOT_MATCH_NEW_PASSWORD_CRITERIA = 1017
        private const val ERROR_CODE_TEMPORARY_BLOCKED_USER = 1031
    }
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
                    viewModel.showProgress(false)
                    when (it.error?.code) {
                        ERROR_CODE_INVALID_OLD_PASSWORD -> {
                            viewModel.showErrorOldPassword.set(true)
                            viewModel.showInvalidOldPasswordError.set(it.error?.message)
                            binding.etOldPassword.setText("")
                            binding.etCreatePassword.setText("")
                            binding.etConfirmPassword.setText("")
                        }
                        ERROR_CODE_TEMPORARY_BLOCKED_USER ->{
                            showLongToast(getString(R.string.block_user_temporary))
                            startLogin(this){
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        }
                        ERROR_CODE_NOT_MATCH_NEW_PASSWORD_CRITERIA -> {
                            showLongToast(getString(R.string.password_criteria))
                             binding.etOldPassword.setText("")
                             binding.etCreatePassword.setText("")
                             binding.etConfirmPassword.setText("")
                         }
                        else -> {
                            showAlertDialog(getString(R.string.failure), it.error?.message, getString(android.R.string.ok))
                        }
                    }
                }
                is Failure -> {
                    showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }


}
