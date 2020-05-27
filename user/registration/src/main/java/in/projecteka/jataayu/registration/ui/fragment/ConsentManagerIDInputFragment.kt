package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.core.model.LoginMode
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.ConsentManagerIdInputFragmentBinding
import `in`.projecteka.jataayu.registration.viewmodel.ConsentManagerIDInputViewModel
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.util.startForgotPassword
import `in`.projecteka.jataayu.util.startRecoverCmid
import `in`.projecteka.jataayu.util.startRegistration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ConsentManagerIDInputFragment : Fragment() {


    companion object {
        fun newInstance() = ConsentManagerIDInputFragment()
    }

    private val viewModel: ConsentManagerIDInputViewModel by viewModel()
    private val loginViewModel: LoginViewModel by sharedViewModel()

    private lateinit var binding: ConsentManagerIdInputFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = ConsentManagerIdInputFragmentBinding.inflate(inflater)
        initBindings()
        initObservers()
        viewModel.init(getString(R.string.cm_config_provider))
        return binding.root
    }

    private fun initBindings() {
        binding.viewModel = viewModel
    }


    private fun initObservers() {

        viewModel.onRegisterButtonClickEvent.observe(viewLifecycleOwner, Observer {
            activity?.let { startRegistration(it) }
        })

        viewModel.onForgetCMIDButtonClickEvent.observe(viewLifecycleOwner, Observer {
            activity?.let { startRecoverCmid(it) }
        })

        viewModel.onNextButtonClickEvent.observe(viewLifecycleOwner, Observer {
            loginViewModel.updateConsentManagerID(viewModel.inputUsernameLbl.get()!!, resources.getString(R.string.cm_config_provider))
            viewModel.fetchLoginMode(loginViewModel.cmId)
        })

        viewModel.loginModeLiveDataResponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    if (!viewModel.isLoginModeHasLoaded) {
                        val loginMode = it.data!!.loginMode
                        viewModel.onLoginModeResponseSuccess(loginMode)
                        if (loginMode == LoginMode.OTP) {
                            startForgotPassword(activity!!) {
                                putExtra(PasswordInputFragment.KEY_CONSENT_ID, loginViewModel.cmId)
                            }
                        } else if (loginMode == LoginMode.PASSWORD) {
                            loginViewModel.replaceFragment(R.layout.password_input_fragment)
                        }
                    }
                }

                is PartialFailure -> {
                    context?.showErrorDialog(it.error?.message)
                }
                is Failure -> {
                    context?.showErrorDialog(it.error.message)
                }
                is Loading -> {
                    viewModel.showProgress(it.isLoading)
                }
            }

        })
    }
}
