package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import `in`.projecteka.jataayu.registration.listener.LoginClickHandler
import `in`.projecteka.jataayu.registration.listener.LoginEnableListener
import `in`.projecteka.jataayu.registration.listener.LoginValuesWatcher
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.FragmentLoginBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.sharedPref.setAuthToken
import `in`.projecteka.jataayu.util.sharedPref.setIsUserLoggedIn
import `in`.projecteka.jataayu.util.startLauncher
import `in`.projecteka.jataayu.util.startRegistration
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseDialogFragment(), LoginClickHandler, LoginEnableListener{
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

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
        startRegistration(activity!!, null)
//        activity?.setResult(Activity.RESULT_FIRST_USER)
//        activity?.finish()
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
                is Loading -> showProgressBar(it.isLoading, getString(R.string.logging_in))
                is Success -> {
                    activity?.setAuthToken(viewModel.getAuthTokenWithTokenType(authToken = it.data?.accessToken, tokenType = it.data?.tokenType))
//                    activity?.setAuthToken(
//                        "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJfOVpfRHhEcXlkb3BEc1FsY18zQ3l6XzYyOEpHMFVuSlRhOVJ2UE00dVVZIn0.eyJqdGkiOiI1YTQxOGZiZS1hYzA5LTRmZTEtYTE5Mi1jMjc2MmQ4YWI1ZjMiLCJleHAiOjE1ODY3ODY5MzAsIm5iZiI6MCwiaWF0IjoxNTg2Nzg1MTMwLCJpc3MiOiJodHRwOi8vMTcyLjE2LjIuMjc6ODA4MS9hdXRoL3JlYWxtcy9jb25zZW50LW1hbmFnZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMjYwODRmNzctZmEwZC00ZDg2LWE1MDktNWMyNGE5OGRjNWZkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY29uc2VudC1zZXJ2aWNlIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiYzQ1ZDUyOTItM2ZiNS00YTViLWE2MjMtMzk0N2EzZTgyODVlIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6ImphbmFraSIsInByZWZlcnJlZF91c2VybmFtZSI6ImphbmFraUBuY2ciLCJnaXZlbl9uYW1lIjoiamFuYWtpIiwiZmFtaWx5X25hbWUiOiIifQ.wTiLPcIzC_WAb9fLAs9HqNjWU4Ip2gN_GMU4gnU43NEFb03lDBA8SH6ZWq4GRhgIvwu9vrLMmbEjhF_ziViqBR_9SYJ0bfbZ0874Bx73-hMpwyMfRgmv9PrmnlDQLA78koPMK1-_vyelivyIBxGQT_cDND1I0tSGCzjVrIxk1h9c1C9OfJZbUJPEE_sKTZ7oCkBwMyP3YQyZ9xyvP2VE1Drcrl-CnhV7Cdalvqvdqx__7xUs4NAo2VzQHrXnt7PiCwHfKOJ2RFPye18hzBqUHM-vuOOuEwhcf5o6YXQPFT6yAnZp5HKS7EDsmv6vJMOus9cbgRDZnBOR6BHzd1yWtw"
//                    )
                    activity?.setIsUserLoggedIn(true)
//                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                    startLauncher(activity!!)

                }
                is PartialFailure -> {
                    context?.showAlertDialog(
                        getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok)
                    )
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
}