package `in`.projecteka.jataayu.registration.ui.fragment


import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.FragmentOtpVerificationBinding
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationVerificationViewModel
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegistrationOtpFragment : BaseFragment() {

    private lateinit var binding: FragmentOtpVerificationBinding

    companion object {
        const val ERROR_CODE_INVALID_OTP = 1003
        const val ERROR_CODE_OTP_EXPIRED = 1004
        const val ERROR_CODE_OTP_LIMIT_EXCEEDED = 1029
        const val EXCEEDED_INVALID_ATTEMPT_LIMIT = 1035
        fun newInstance() = RegistrationOtpFragment()
    }

    private val parentVM: RegistrationActivityViewModel by sharedViewModel()

    private val viewModel: RegistrationVerificationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpVerificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        parentVM.appBarTitle.set(getString(R.string.verification))

        viewModel.otpMessageLbl.set(
            SpannableStringBuilder()
                .append(getString(R.string.otp_sent))
        )
        viewModel.mobileNumberText.set(parentVM.getIdentifierValue())
        initObservers()
    }

    private fun initObservers() {
        viewModel.onClickVerifyEvent.observe(this, Observer {
            parentVM.verifyRequest(it)
        })

        viewModel.onClickResendEvent.observe(this, Observer {
            parentVM.requestVerification(it)
        })

        parentVM.verifyIdentifierResponseLiveData.observe(this, Observer {
            when (it) {
                is PartialFailure -> {
                    if (it.error?.code == ERROR_CODE_INVALID_OTP || it.error?.code == EXCEEDED_INVALID_ATTEMPT_LIMIT ) {
                        viewModel.otpText.set(null)
                    }
                    viewModel.errorLbl.set(
                        when (it.error?.code) {
                            ERROR_CODE_INVALID_OTP -> {
                                getString(R.string.invalid_otp)
                            }
                            ERROR_CODE_OTP_EXPIRED -> {
                                getString(R.string.otp_expired)
                            }
                            EXCEEDED_INVALID_ATTEMPT_LIMIT -> {
                                getString(R.string.exceeded_otp_attempt_limit)
                            }
                            else -> it.error?.message
                        }
                    )
                }
            }
        })

        parentVM.requestVerificationResponseLiveData.observe(this, Observer {
            when (it) {
                is PartialFailure -> {
                    viewModel.otpText.set(null)
                    if (it.error?.code == ERROR_CODE_OTP_LIMIT_EXCEEDED) {
                        viewModel.errorLbl.set(getString(R.string.otp_limit_exceeded))
                    } else {
                        viewModel.errorLbl.set(it.error?.message)
                    }
                }
            }
        })
    }
}
