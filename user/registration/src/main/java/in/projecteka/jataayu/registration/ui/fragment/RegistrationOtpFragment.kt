package `in`.projecteka.jataayu.registration.ui.fragment


import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.FragmentOtpVerificationBinding
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationVerificationViewModel
import `in`.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.lifecycle.Observer
import okhttp3.internal.format
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegistrationOtpFragment : BaseFragment() {

    private lateinit var binding: FragmentOtpVerificationBinding

    companion object {

        private const val ERROR_CODE_INVALID_OTP = 1003
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

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.verification)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.otpMessageLbl.set(SpannableStringBuilder()
            .append(getString(R.string.otp_sent))
            .bold { append(" ${parentVM.getIdentifierValue()}")})

        initObservers()
    }

    private fun initObservers() {
        viewModel.onClickVerifyEvent.observe(this, Observer {
            parentVM.verifyRequest(it)
        })
        parentVM.verifyIdentifierResponseLiveData.observe(this, Observer {
            when (it) {
                is PartialFailure -> {
                    viewModel.errorLbl.set(
                        if (it.error?.code == ERROR_CODE_INVALID_OTP)
                            getString(R.string.invalid_otp)
                        else
                            it.error?.message
                    )
                }
            }
        })
    }
}
