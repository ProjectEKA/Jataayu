package `in`.org.projecteka.jataayu.registration.ui.fragment

import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.registration.R
import `in`.org.projecteka.jataayu.registration.databinding.FragmentRegistrationBinding
import `in`.org.projecteka.jataayu.registration.listener.ContinueClickHandler
import `in`.org.projecteka.jataayu.registration.listener.MobileNumberChangeHandler
import `in`.org.projecteka.jataayu.registration.listener.MobileNumberChangeWatcher
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.org.projecteka.jataayu.registration.ui.activity.RegistrationActivity
import `in`.org.projecteka.jataayu.registration.viewmodel.RegistrationViewModel
import `in`.org.projecteka.jataayu.util.extension.setTitle
import `in`.org.projecteka.jataayu.util.extension.showLongToast
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_registration.*
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegistrationFragment : BaseFragment(), ContinueClickHandler, MobileNumberChangeHandler, ProgressDialogCallback {
    private lateinit var binding: FragmentRegistrationBinding

    private val viewModel: RegistrationViewModel by sharedViewModel()
    private val registrationObserver = Observer<RequestVerificationResponse> {
        showLongToast(it.sessionId)
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
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.register)
    }

    override fun onSuccess(any: Any?) {
        showProgressBar(false)
    }

    override fun onFailure(any: Any?) {
        showProgressBar(false)
    }

    override fun onContinueClick(view: View) {
        showProgressBar(true, getString(R.string.sending_otp))
        viewModel.requestVerification("mobile", binding.etMobileNumber.text.toString(), this)
        viewModel.requestVerificationResponse.observe(this, registrationObserver)
    }

    override fun setButtonEnabled(boolean: Boolean) {
        binding.isValidMobileNumber = boolean
    }
}