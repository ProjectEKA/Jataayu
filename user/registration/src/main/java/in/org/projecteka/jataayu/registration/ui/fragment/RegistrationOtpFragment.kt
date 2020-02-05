package `in`.org.projecteka.jataayu.registration.ui.fragment


import `in`.org.projecteka.jataayu.core.databinding.VerityOtpFragmentBinding
import `in`.org.projecteka.jataayu.core.handler.OtpChangeHandler
import `in`.org.projecteka.jataayu.core.handler.OtpChangeWatcher
import `in`.org.projecteka.jataayu.core.model.handler.OtpSubmissionClickHandler
import `in`.org.projecteka.jataayu.core.utils.toErrorResponse
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.registration.R
import `in`.org.projecteka.jataayu.registration.listener.MobileNumberChangeHandler
import `in`.org.projecteka.jataayu.registration.model.RequestVerificationResponse
import `in`.org.projecteka.jataayu.registration.viewmodel.RegistrationViewModel
import `in`.org.projecteka.jataayu.util.extension.EMPTY
import `in`.org.projecteka.jataayu.util.extension.setTitle
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import retrofit2.Response

class RegistrationOtpFragment : BaseFragment(), OtpSubmissionClickHandler, ProgressDialogCallback, MobileNumberChangeHandler,
    OtpChangeHandler {

    private lateinit var binding: VerityOtpFragmentBinding

    override fun setButtonEnabled(boolean: Boolean) {
        binding.isOtpEntered = boolean
    }

    override fun onSuccess(any: Any?) {
        showProgressBar(false)
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun onFailure(any: Any?) {
        showProgressBar(false)
        if((any is Response<*>)) {
            val errorResponse = any.errorBody()?.toErrorResponse()
            if(errorResponse?.error?.code == ERROR_CODE_INVALID_OTP)
                binding.errorMessage = context?.getString(R.string.invalid_otp)
        }
    }

    companion object{
        const val ERROR_CODE_INVALID_OTP = 1001
        fun newInstance() = RegistrationOtpFragment()
    }

    private val eventBus: EventBus by lazy { EventBus.getDefault() }
    private val viewModel: RegistrationViewModel by sharedViewModel()
    private lateinit var requestVerificationResponse: RequestVerificationResponse

    override fun onSubmitOtp(view: View) {
        binding.errorMessage = String.EMPTY
        showProgressBar(true)
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
        initBinding()
    }

    private fun initBinding() {
        binding.clickHandler = this
        binding.isOtpEntered = false
        binding.errorMessage = String.EMPTY
        binding.otpChangeWatcher = OtpChangeWatcher(this)
    }
}
