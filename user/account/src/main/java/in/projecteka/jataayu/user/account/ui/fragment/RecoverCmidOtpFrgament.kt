package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.RecoverCmidOtpFrgamentBinding
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidOtpFragmentViewModel
import `in`.projecteka.jataayu.util.extension.showSnackbar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecoverCmidOtpFrgament : Fragment() {

    companion object {
        fun newInstance() = RecoverCmidOtpFrgament()
    }

    private val viewModel: RecoverCmidOtpFragmentViewModel by viewModel()
    private val recoverCmidActivityViewModel: RecoverCmidActivityViewModel by sharedViewModel()
    private lateinit var binding: RecoverCmidOtpFrgamentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecoverCmidOtpFrgamentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initObservers()
    }

    fun initObservers() {
        binding.btnValidate.setOnClickListener {
            viewModel.verifyOtp(recoverCmidActivityViewModel.generateOTPResponse!!.sessionId)
        }
        binding.resendOtp.setOnClickListener {
            viewModel.resendOTP(recoverCmidActivityViewModel.otpRequest!!)
        }

        viewModel.verifyOTPResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
                    recoverCmidActivityViewModel.onDisplayCmidRequest(it.data!!)
                }
                is PartialFailure -> {
                    it.error?.let { partialFailure -> viewModel.onVerifyOTPResponseFailure(partialFailure, resources) }
                }
                is Failure -> {
                    activity?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })

        viewModel.recoverCmidResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
                    // show snackbar
                    showSnackbar(binding.layoutRoot,getString(R.string.otp_sent_msg))
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
    }
}
