package `in`.projecteka.resetpassword.ui.fragment


import `in`.projecteka.forgotpassword.R
import `in`.projecteka.forgotpassword.databinding.ResetPasswordOtpVerificationBinding
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordActivityViewModel
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordOtpVerificationViewModel
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.reset_password_otp_verification.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResetPasswordOtpFragment : BaseFragment() {

    private lateinit var binding: ResetPasswordOtpVerificationBinding
    private var textView: TextView? = null

    companion object {
        private const val ERROR_CODE_INVALID_OTP = 1003
        private const val ERROR_CODE_OTP_EXPIRED = 1004
        const val EXCEEDED_INVALID_ATTEMPT_LIMIT = 1035
        fun newInstance() = ResetPasswordOtpFragment()
        private lateinit var snackbar: Snackbar
    }

    private val parentViewModel: ResetPasswordActivityViewModel by sharedViewModel()

    private val viewModel: ResetPasswordOtpVerificationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ResetPasswordOtpVerificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initObservers()
        viewModel.generateOtp(parentViewModel.consentManagerId)
        parentViewModel.tempToken?.let {
            viewModel.init(it)
        }
    }

    private fun initObservers() {
        viewModel.generateOtpResponse.observe(this, Observer {
            when(it){
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
                    parentViewModel.sessionId = it.data?.sessionId
                    showSnackbar(String.format(getString(R.string.otp_sent_msg), parentViewModel.consentManagerId))
                }
                is PartialFailure -> {
                    activity?.showAlertDialog(
                        getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok)
                    )
                }
                is Failure -> {
                    activity?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })
        viewModel.onClickValidateEvent.observe(this, Observer {
            parentViewModel.sessionId?.let {
                viewModel.verifyOtp(
                    it,
                    viewModel.otpText.get().toString()
                )
            }
        })

        viewModel.verifyOtpResponse.observe(this, Observer {
            when (it) {
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
                    parentViewModel.tempToken = it.data?.temporaryToken
                    parentViewModel.onVerifyOtpRedirectRequest()
                    if (snackbar.isShown) snackbar.dismiss()
                }
                is PartialFailure -> {
                    if (it.error?.code == ERROR_CODE_INVALID_OTP || it.error?.code == ERROR_CODE_OTP_EXPIRED || it.error?.code == EXCEEDED_INVALID_ATTEMPT_LIMIT) {
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
                is Failure -> {
                    activity?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }

    private fun showSnackbar(message: String) {
        val spannableString = SpannableString(message)

        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        snackbar = Snackbar.make(snackbar_container, spannableString, Snackbar.LENGTH_LONG)

        val layout = snackbar.view
        if (textView == null) {
            textView = layout.findViewById(com.google.android.material.R.id.snackbar_text) as? TextView
            textView?.maxLines = 10
        }
        if (!snackbar.isShown) snackbar.show()
    }

    override fun onPause() {
        super.onPause()
        if (snackbar.isShown) snackbar.dismiss()
    }
}