package `in`.projecteka.jataayu.provider.ui.fragment

import `in`.projecteka.featuresprovider.R
import `in`.projecteka.featuresprovider.databinding.VerityOtpFragmentBinding
import `in`.projecteka.jataayu.core.ProviderLinkType
import `in`.projecteka.jataayu.core.handler.OtpSubmissionClickHandler
import `in`.projecteka.jataayu.network.interceptor.NoConnectivityException
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.activity.NoInternetConnectionActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.provider.model.Otp
import `in`.projecteka.jataayu.provider.model.SuccessfulLinkingResponse
import `in`.projecteka.jataayu.provider.viewmodel.ProviderActivityViewModel
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.startDashboard
import `in`.projecteka.jataayu.util.ui.UiUtils
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class VerifyOtpFragment : BaseFragment(),
    OtpSubmissionClickHandler, ResponseCallback {
    private lateinit var binding: VerityOtpFragmentBinding
    private var isNoNetworkScreenShown = false

    companion object {
        fun newInstance() = VerifyOtpFragment()
        const val ERROR_CODE_INVALID_OTP = 1003
        const val ERROR_CODE_GATEWAY_TIMEOUT = 1036
    }

    private val viewModel : ProviderSearchViewModel by sharedViewModel()
    private val parentViewModel : ProviderActivityViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VerityOtpFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        viewModel.successfulLinkingResponse.observe(this, observer)

    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.selectedProviderName = viewModel.selectedProviderName
        binding.mobile = viewModel.linkAccountsResponse.value?.link?.meta?.communicationHint
        binding.clickHandler = this
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.verification)
    }

    private val observer = Observer<SuccessfulLinkingResponse> {
        viewModel.preferenceRepository.hasProviders = true

        if (parentViewModel.providerLinkType.get() == ProviderLinkType.LINK_WHILE_GRANT) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        } else {
            activity?.finish()
            startDashboard(activity!!){
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
    }

    override fun onSubmitOtp(view: View) {
        UiUtils.hideKeyboard(activity!!)
        viewModel.showProgress(true)
        val referenceNumber = viewModel.linkAccountsResponse.value?.link?.referenceNumber!!
        val otp = Otp(binding.etOtp.text.toString())
        viewModel.verifyOtp(referenceNumber, otp, this)
    }

    override fun <T> onSuccess(body: T?) {
        viewModel.showProgress(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        viewModel.showProgress(false)

        if (errorBody.error.code == ERROR_CODE_INVALID_OTP) {
            viewModel.otpText.set(null)

            viewModel.errorLbl.set(
                if (errorBody.error.code == ERROR_CODE_INVALID_OTP) {
                    getString(R.string.invalid_otp)
                }
                else
                    errorBody.error.message
            )
        } else if (errorBody.error.code == ERROR_CODE_GATEWAY_TIMEOUT) {
            binding.errorMessage = getString(R.string.something_went_wrong_try_again)
        }else {
            binding.errorMessage = errorBody.error.message
        }
    }

    override fun onFailure(t: Throwable) {
        viewModel.showProgress(false)
        if (t is NoConnectivityException) {
            if (!isNoNetworkScreenShown) {
                NoInternetConnectionActivity.start(context!!) {
                    onSubmitOtp(View(context))
                    isNoNetworkScreenShown = false
                }
                isNoNetworkScreenShown = true
            }
        } else {
            context?.showErrorDialog(t.localizedMessage)
        }
        context?.showErrorDialog(t.localizedMessage)
    }
}