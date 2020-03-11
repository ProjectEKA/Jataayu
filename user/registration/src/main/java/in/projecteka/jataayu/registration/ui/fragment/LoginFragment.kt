package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import `in`.projecteka.jataayu.registration.listener.LoginClickHandler
import `in`.projecteka.jataayu.registration.listener.LoginEnableListener
import `in`.projecteka.jataayu.registration.listener.LoginValuesWatcher
import `in`.projecteka.jataayu.registration.ui.activity.LoginActivity
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.FragmentLoginBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.sharedPref.setAuthToken
import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseDialogFragment(), LoginClickHandler, LoginEnableListener,
    ResponseCallback {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by sharedViewModel()

    companion object {
        const val SPACE = " "
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onRegisterClick(view: View) {
        activity?.setResult(Activity.RESULT_FIRST_USER)
        activity?.finish()
    }

    override fun onLoginClick(view: View) {
        viewModel.login(getUsername(), binding.etPassword.text.toString())
    }

    private fun getProviderName(): String {
        return binding.tvProviderName.text.toString()
    }

    private fun getUsername(): String {
        return et_username?.text.toString() + getProviderName()
    }

    override fun showOrHidePassword(view: View) {
        when (binding.etPassword.inputType) {
            getPasswordInputType() -> {
                binding.passwordInputType = getVisiblePasswordInputType()
                binding.btnShowHidePassword.text = getString(R.string.hide)
            }
            getVisiblePasswordInputType() -> {
                binding.passwordInputType = getPasswordInputType()
                binding.btnShowHidePassword.text = getString(R.string.show)
            }
        }
        binding.etPassword.post { binding.etPassword.setSelection(binding.etPassword.text.length) }
    }

    private fun getVisiblePasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }


    private fun getPasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initBindings()
    }

    private fun initObservers() {
        viewModel.loginResponse.observe(this, Observer {
            when (it) {
                is Loading -> showProgressBar(it.isLoading, "Logging in...")
                is Success -> {
                    context?.setAuthToken(
                        viewModel.getAuthTokenWithTokenType(
                            authToken = it.data?.accessToken,
                            tokenType = it.data?.tokenType
                        )
                    )
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
                is PartialFailure-> {
                    context?.showAlertDialog(getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok))
                }
                is Failure -> {
                    context?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }

    private fun initBindings() {
        binding.passwordInputType = getPasswordInputType()
        binding.loginClickHandler = this
        binding.enableLogin = false
        binding.loginValuesWatcher = LoginValuesWatcher(this)
    }

    override fun enableLoginButton() {
        binding.enableLogin = et_username.text.isNotEmpty() && et_password.text.isNotEmpty()
    }

    override fun <T> onSuccess(body: T?) {
        (activity as LoginActivity).showProgressBar(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        showProgressBar(false)
        context?.showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        context?.showErrorDialog(t.localizedMessage)
    }
}