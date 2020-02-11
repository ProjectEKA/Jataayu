package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.registration.R
import `in`.projecteka.jataayu.registration.databinding.FragmentRegistrationBinding
import `in`.projecteka.jataayu.registration.listener.ContinueClickHandler
import `in`.projecteka.jataayu.registration.listener.MobileNumberChangeHandler
import `in`.projecteka.jataayu.registration.listener.MobileNumberChangeWatcher
import `in`.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.projecteka.jataayu.registration.ui.activity.RegistrationActivity
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationViewModel.Companion.MOBILE_IDENTIFIER_TYPE
import `in`.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_registration.*
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegistrationFragment : BaseFragment(), ContinueClickHandler, MobileNumberChangeHandler, ResponseCallback {
    private lateinit var binding: FragmentRegistrationBinding

    private val viewModel: RegistrationViewModel by sharedViewModel()
    private val registrationObserver = Observer<RequestVerificationResponse> {
        (activity as RegistrationActivity).redirectToOtpScreen()
        EventBus.getDefault().postSticky(et_mobile_number.text.toString())
        EventBus.getDefault().postSticky(it)
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandler = this
        binding.mobileNumberWatcher = MobileNumberChangeWatcher(this)
        binding.isValidMobileNumber = false
        binding.countryCode = viewModel.getCountryCode()
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.register)
    }

    override fun onContinueClick(view: View) {
        showProgressBar(true, getString(R.string.sending_otp))
        viewModel.requestVerification(MOBILE_IDENTIFIER_TYPE, viewModel.getMobileNumber(binding.etMobileNumber.text.toString()), this)
        viewModel.requestVerificationResponse.observe(this, registrationObserver)
    }

    override fun setButtonEnabled(boolean: Boolean) {
        binding.isValidMobileNumber = boolean
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ResponseBody) {
        showProgressBar(false)
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
    }
}