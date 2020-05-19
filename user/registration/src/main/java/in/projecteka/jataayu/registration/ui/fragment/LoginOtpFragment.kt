package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.LoginOtpFragmentBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginOtpViewModel
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginOtpFragment : Fragment() {

    companion object {
        const val ERROR_CODE_INVALID_OTP = 1003
        const val ERROR_CODE_OTP_EXPIRED = 1004
        const val ERROR_CODE_OTP_LIMIT_EXCEED = 1035
        fun newInstance() = LoginOtpFragment()
    }

    private val viewModel: LoginOtpViewModel by viewModel()
    private val loginViewModel: LoginViewModel by sharedViewModel()
    private lateinit var binding: LoginOtpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginOtpFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        initObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.generateOtp(loginViewModel.cmId)
    }

    private fun initObservers() {

        binding.btnContinue.setOnClickListener {
            viewModel.verifyLoginOtp(binding.etOtp.text.toString())
        }

       viewModel.otpSessionResponseLiveData.observe(viewLifecycleOwner, Observer {
           when(it) {
               is Loading -> { viewModel.showProgress(it.isLoading, R.string.sending_otp)}
               is Success ->  {
                   viewModel.otpMessageLbl.set(
                       SpannableStringBuilder()
                           .append(getString(R.string.otp_sent))
                   )
                   it.data?.let { viewModel.onOtpSessionCreateSuccess(loginViewModel.cmId, it) }
               }
               is PartialFailure ->  {
                   activity?.showErrorDialog(it.error?.message)
               }

               is Failure -> {
                   activity?.showErrorDialog(getString(R.string.something_went_wrong))
               }
           }
       })

        viewModel.loginByOTPResponseLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Loading -> { viewModel.showProgress(it.isLoading, R.string.sending_otp)}
                is Success ->  {
                    viewModel.onLoginSuccess(it.data!!)
                    loginViewModel.loginResponseSuccessEvent.call()
                }
                is PartialFailure ->  {
                    if (it.error?.code == ERROR_CODE_INVALID_OTP) {
                        viewModel.otpText.set(null)
                        viewModel.errorLbl.set(getString(R.string.invalid_otp))
                    }

                    if(it.error?.code == ERROR_CODE_OTP_EXPIRED) {
                        viewModel.otpText.set(null)
                        viewModel.errorLbl.set(getString(R.string.otp_expired))
                    }
                    if(it.error?.code == ERROR_CODE_OTP_LIMIT_EXCEED) {
                        viewModel.otpText.set(null)
                        viewModel.errorLbl.set(getString(R.string.otp_limit_exceeded))
                    }
                }
                is Failure -> {
                    activity?.showErrorDialog(getString(R.string.something_went_wrong))
                }
            }
        })

    }


}
