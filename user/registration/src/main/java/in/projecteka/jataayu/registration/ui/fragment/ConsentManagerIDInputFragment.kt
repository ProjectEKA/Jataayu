package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.ConsentManagerIdInputFragmentBinding
import `in`.projecteka.jataayu.registration.viewmodel.ConsentManagerIDInputViewModel
import `in`.projecteka.jataayu.registration.viewmodel.LoginMode.OTP
import `in`.projecteka.jataayu.registration.viewmodel.LoginMode.PASSWORD
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
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

    private val loginMode = PASSWORD

    companion object {
        fun newInstance() = ConsentManagerIDInputFragment()
    }

    private val viewModel: ConsentManagerIDInputViewModel by viewModel()
    private val loginViewModel: LoginViewModel by sharedViewModel()

    private lateinit var binding: ConsentManagerIdInputFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = ConsentManagerIdInputFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        initObservers()
        return binding.root
    }


    private fun initObservers() {

        viewModel.onRegisterButtonClickEvent.observe(viewLifecycleOwner, Observer {
            activity?.let { startRegistration(it) }
        })

        viewModel.onForgetCMIDButtonClickEvent.observe(viewLifecycleOwner, Observer {

        })

        viewModel.onNextButtonClickEvent.observe(viewLifecycleOwner, Observer {
            // TODO - should remove after API integration
            loginViewModel.updateConsentManagerID(viewModel.inputUsernameLbl.get()!!, resources.getString(R.string.cm_config_provider))
            when(loginMode) {
                OTP -> loginViewModel.replaceFragment(R.layout.login_otp_fragment)
                PASSWORD -> loginViewModel.replaceFragment(R.layout.password_input_fragment)
            }
        })
    }
}
