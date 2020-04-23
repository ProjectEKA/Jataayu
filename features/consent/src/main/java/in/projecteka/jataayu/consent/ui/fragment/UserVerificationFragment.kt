package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.UserVerificationFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.PinVerificationViewModel
import `in`.projecteka.jataayu.consent.viewmodel.UserVerificationViewModel
import `in`.projecteka.jataayu.core.handler.OtpChangeHandler
import `in`.projecteka.jataayu.core.handler.OtpChangeWatcher
import `in`.projecteka.jataayu.core.handler.OtpSubmissionClickHandler
import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import `in`.projecteka.jataayu.presentation.wobble
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.sharedPref.setConsentTempToken
import `in`.projecteka.jataayu.util.ui.UiUtils
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class UserVerificationFragment : BaseDialogFragment(), OtpSubmissionClickHandler, OtpChangeHandler, ResponseCallback {
    private lateinit var binding: UserVerificationFragmentBinding

    private val parentViewModel: PinVerificationViewModel by sharedViewModel()

    companion object {
        fun newInstance() = UserVerificationFragment()
        private const val ERROR_CODE_INVALID_PIN = 1022
        private const val ERROR_CODE_TOKEN_INVALID = 1017
    }

    private var viewModel = UserVerificationViewModel(get())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserVerificationFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBindings()
        initObservers()
    }

    private fun initObservers() {
        viewModel.userVerificationResponse.observe(this, Observer { it ->
            context?.setConsentTempToken(it.temporaryToken)
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        })
    }

    private fun initBindings() {
        binding.clickHandler = this
        binding.isOtpEntered = false
        binding.otpChangeWatcher = OtpChangeWatcher(4, this)
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.grant_request)
    }

    override fun onSubmitOtp(view: View) {
        binding.lblInvalidPin.visibility = View.GONE
        UiUtils.hideKeyboard(activity!!)
        val pin = binding.etPin.text.toString()
        showProgressBar(true)
        viewModel.verifyUser(pin, this)
    }

    override fun setButtonEnabled(isOtpEntered: Boolean) {
        binding.isOtpEntered = true
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        showProgressBar(false)

        when (errorBody.error.code) {
            ERROR_CODE_INVALID_PIN -> {
                binding.lblInvalidPin.visibility = View.VISIBLE
                binding.etPin.setText("")
                binding.etPin.wobble()
            }
            ERROR_CODE_TOKEN_INVALID -> {
                startActivity(Intent().apply {
                    action = UnauthorisedUserRedirectInterceptor.REDIRECT_ACTIVITY_ACTION
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
            else -> {
                context?.showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
            }
        }
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        context?.showErrorDialog(t.localizedMessage)
    }
}
