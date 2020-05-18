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
        const val ERROR_CODE_OTP_LIMIT_EXCEEDED = 1029
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
        viewModel.onClickVerifyEvent.observe(viewLifecycleOwner, Observer {
            parentVM.verifyRequest(it)
        })

        viewModel.onClickResendEvent.observe(viewLifecycleOwner, Observer {
            parentVM.requestVerification(it)
        })

        parentVM.verifyIdentifierResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PartialFailure -> {
                    if (it.error?.code == ERROR_CODE_INVALID_OTP) {
                        viewModel.otpText.set(null)
                    }

                    viewModel.errorLbl.set(
                        if (it.error?.code == ERROR_CODE_INVALID_OTP) {
                            getString(R.string.invalid_otp)
                        } else
                            it.error?.message
                    )
                }
            }
        })

        parentVM.requestVerificationResponseLiveData.observe(viewLifecycleOwner, Observer {
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
