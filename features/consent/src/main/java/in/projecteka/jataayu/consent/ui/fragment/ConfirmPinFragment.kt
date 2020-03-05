package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.ConfirmPinFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.UserVerificationViewModel
import `in`.projecteka.jataayu.core.handler.OtpChangeHandler
import `in`.projecteka.jataayu.core.handler.OtpChangeWatcher
import `in`.projecteka.jataayu.core.handler.OtpSubmissionClickHandler
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import `in`.projecteka.jataayu.presentation.wobble
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.ui.UiUtils
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import okhttp3.ResponseBody
import org.koin.android.ext.android.get

private const val PIN = "PIN"

class ConfirmPinFragment : BaseDialogFragment(), OtpSubmissionClickHandler, OtpChangeHandler, ResponseCallback {
    private lateinit var binding: ConfirmPinFragmentBinding

    companion object {
        fun newInstance(otp: String): ConfirmPinFragment {
            val bundle = Bundle()
            bundle.putString(PIN, otp)
            val createPinFragment = ConfirmPinFragment()
            createPinFragment.arguments = bundle
            return createPinFragment
        }
    }

    private var viewModel = UserVerificationViewModel(get())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmPinFragmentBinding.inflate(inflater)
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
        setTitle(R.string.create_consent_pin)
    }

    override fun onSubmitOtp(view: View) {
        binding.lblInvalidPin.visibility = GONE
        UiUtils.hideKeyboard(activity!!)
        val confirmedPin = binding.etPin.text.toString()
        arguments?.let { bundle ->
            bundle.getString(PIN)?.let { pin ->
                if (confirmedPin == pin) {
                    showProgressBar(true)
                    viewModel.createPinResponse.observe(this, Observer {
                        activity?.let {
                            it.setResult(Activity.RESULT_OK)
                            it.finish()
                        }
                    })
                    viewModel.createPin(confirmedPin, this)
                } else {
                    binding.lblInvalidPin.visibility = VISIBLE
                    binding.etPin.setText("")
                    binding.etPin.wobble()
                }
            }
        }
    }

    override fun setButtonEnabled(isOtpEntered: Boolean) {
        binding.isOtpEntered = true
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ResponseBody) {
        showProgressBar(false)
        context?.showAlertDialog(getString(R.string.failure), errorBody.string(), getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        context?.showErrorDialog(t.localizedMessage)
    }
}
