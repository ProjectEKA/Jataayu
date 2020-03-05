package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.UserVerificationFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.UserVerificationViewModel
import `in`.projecteka.jataayu.core.handler.OtpChangeHandler
import `in`.projecteka.jataayu.core.handler.OtpChangeWatcher
import `in`.projecteka.jataayu.core.handler.OtpSubmissionClickHandler
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import `in`.projecteka.jataayu.presentation.wobble
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.ui.UiUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.get


class UserVerificationFragment : BaseDialogFragment(), OtpSubmissionClickHandler, OtpChangeHandler, ResponseCallback {
    private lateinit var binding: UserVerificationFragmentBinding

    companion object {
        fun newInstance() = UserVerificationFragment()
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
        viewModel.userVerificationResponse.observe(this, Observer { userVerificationResponse ->
            if (userVerificationResponse.isValid) {
                EventBus.getDefault().post(MessageEventType.USER_VERIFIED)
            } else {
                binding.lblInvalidPin.visibility = View.VISIBLE
                binding.etPin.setText("")
                binding.etPin.wobble()
            }
        })
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
        context?.showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        context?.showErrorDialog(t.localizedMessage)
    }
}
