package `in`.org.projecteka.jataayu.registration.ui.fragment


import `in`.org.projecteka.jataayu.core.databinding.VerityOtpFragmentBinding
import `in`.org.projecteka.jataayu.core.model.handler.OtpSubmissionClickHandler
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.registration.R
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.org.projecteka.jataayu.registration.model.VerifyIdentifierResponse
import `in`.org.projecteka.jataayu.registration.ui.activity.RegistrationActivity
import `in`.org.projecteka.jataayu.registration.viewmodel.RegistrationViewModel
import `in`.org.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegistrationOTPFragment : BaseFragment(), OtpSubmissionClickHandler, ProgressDialogCallback {
    override fun onSuccess(any: Any?) {

    }

    override fun onFailure(any: Any?) {
    }

    private lateinit var binding: VerityOtpFragmentBinding

    companion object{

        fun newInstance() = RegistrationOTPFragment()
    }
    private val eventBus: EventBus by lazy { EventBus.getDefault() }
    private val viewModel: RegistrationViewModel by sharedViewModel()
    private lateinit var requestVerificationResponse: RequestVerificationResponse

    private val observer = Observer<VerifyIdentifierResponse> {
        (activity as RegistrationActivity).redirectToProviderSearchScreen()
    }

    override fun onSubmitOtp(view: View) {
        viewModel.verifyIdentifier(requestVerificationResponse, this)
    }

    override fun onStart() {
        super.onStart()
        if(!eventBus.isRegistered(this)) {
            eventBus.register(this)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VerityOtpFragmentBinding.inflate(inflater)
        return binding.root
    }

    @Subscribe(sticky = true)
    fun onMobileNumberReceived(mobileNumber: String) {
        mobileNumber?.let {
            binding.message = String.format(getString(R.string.otp_sent), it)
            eventBus.removeStickyEvent(mobileNumber)
        }
    }

    @Subscribe(sticky = true)
    fun onRequestVerificationResponseReceived(event: RequestVerificationResponse){
        event.let {
            requestVerificationResponse = it
            eventBus.removeStickyEvent(event)
        }
    }

    override fun onStop() {
        eventBus.unregister(this)
        super.onStop()
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.verification)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandler = this
        viewModel.verifyIdentifierResponse.observe(this, observer)
    }
}
