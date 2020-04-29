package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.UserVerificationFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.PinVerificationViewModel
import `in`.projecteka.jataayu.consent.viewmodel.UserVerificationViewModel
import `in`.projecteka.jataayu.core.handler.OtpChangeHandler
import `in`.projecteka.jataayu.core.handler.OtpChangeWatcher
import `in`.projecteka.jataayu.core.handler.OtpSubmissionClickHandler
import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import `in`.projecteka.jataayu.presentation.wobble
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.ui.UiUtils
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserVerificationFragment : BaseDialogFragment(), OtpSubmissionClickHandler, OtpChangeHandler {
    private lateinit var binding: UserVerificationFragmentBinding

    private val parentViewModel: PinVerificationViewModel by sharedViewModel()

    companion object {
        fun newInstance() = UserVerificationFragment()
        private const val ERROR_CODE_INVALID_PIN = 1022
        private const val ERROR_CODE_TOKEN_INVALID = 1017
    }

    private val viewModel: UserVerificationViewModel by viewModel()

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
            when (it) {
                is Loading -> showProgressBar(true)
                is Success -> {
                    viewModel.credentialsRepository.consentTemporaryToken = it.data?.temporaryToken
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
                is PartialFailure -> {
                    when (it.error?.code) {
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
                            context?.showAlertDialog(getString(R.string.failure), it.error?.message, getString(android.R.string.ok))
                        }
                    }
                }
                is Failure -> {
                    context?.showErrorDialog(it.error.localizedMessage)
                }
            }
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
        viewModel.verifyUser(pin)
    }

    override fun setButtonEnabled(isOtpEntered: Boolean) {
        binding.isOtpEntered = true
    }
}
