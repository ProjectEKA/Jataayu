package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.registration.ui.activity.LoginActivity
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.PasswordInputFragmentBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.registration.viewmodel.PasswordInputViewModel
import `in`.projecteka.jataayu.util.startForgotPassword
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordInputFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordInputFragment()
    }

    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val viewModel: PasswordInputViewModel by viewModel()

    private lateinit var binding: PasswordInputFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PasswordInputFragmentBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        initObserver()
        return binding.root
    }

    private fun initObserver() {

        viewModel.onPasswordVisibilityToggleEvent.observe(viewLifecycleOwner, Observer {
            binding.etPassword.setSelection(it)
        })

        binding.btnLogin.setOnClickListener {
            viewModel.onLoginClicked(loginViewModel.cmId)
        }

        viewModel.loginByPasswordResponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Loading -> viewModel.showProgress(it.isLoading, R.string.logging_in)
                is Success -> {
                    viewModel.credentialsRepository.accessToken =
                        "${it.data?.tokenType?.capitalize()} ${it.data?.accessToken}"
                    viewModel.preferenceRepository.isUserLoggedIn = true
                    viewModel.credentialsRepository.refreshToken = it.data?.refreshToken
                    loginViewModel.loginResponseSuccessEvent.call()
                }
                is PartialFailure -> {
                    when (it.error?.code) {
                        LoginActivity.ERROR_CODE_BLOCK_USER -> {
                            viewModel.accountLockBlockEnable.set(View.VISIBLE)
                            viewModel.accountLockBlockDividerEnable.set(View.VISIBLE)
                        }
                        else -> {
                            context?.showAlertDialog(
                                getString(R.string.failure), it.error?.message,
                                getString(android.R.string.ok)
                            )
                        }
                    }
                }
                is Failure -> {
                    context?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })

        viewModel.onClickForgotPasswordEvent.observe(viewLifecycleOwner, Observer {
            activity?.let { startForgotPassword(it) }
        })
    }

}
