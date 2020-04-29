package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.ConfirmPinFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.UserVerificationViewModel
import `in`.projecteka.jataayu.core.handler.OtpChangeHandler
import `in`.projecteka.jataayu.core.handler.OtpChangeWatcher
import `in`.projecteka.jataayu.core.handler.OtpSubmissionClickHandler
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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val PIN = "PIN"

class ConfirmPinFragment : BaseDialogFragment(), OtpSubmissionClickHandler, OtpChangeHandler {
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

    private val viewModel: UserVerificationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmPinFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.createPinResponse.observe(this, Observer {

            when (it) {
                is Loading -> showProgressBar(it.isLoading, getString(R.string.creating_pin))
                is Success -> {
                    activity?.let {
                        viewModel.preferenceRepository.pinCreated = true
                        showProgressBar(true)
                        viewModel.verifyUser(binding.etPin.text.toString())
                    }
                }
                is PartialFailure -> {
                    context?.showAlertDialog(getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok))
                }
            }
        })

        viewModel.userVerificationResponse.observe(this, Observer { userVerificationResponse ->
            when (userVerificationResponse) {
                is Loading -> showProgressBar(userVerificationResponse.isLoading, getString(R.string.verifying_pin))
                is Success -> {
                    viewModel.credentialsRepository.consentTemporaryToken = userVerificationResponse.data?.temporaryToken
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
                is PartialFailure -> {
                    context?.showAlertDialog(
                        getString(R.string.failure), userVerificationResponse.error?.message,
                        getString(android.R.string.ok)
                    )
                }
                is Failure -> {
                    context?.showErrorDialog(userVerificationResponse.error.localizedMessage)
                    activity?.setResult(Activity.RESULT_CANCELED)
                    activity?.finish()
                }
            }
        })
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
                    viewModel.createPin(confirmedPin)
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
}
