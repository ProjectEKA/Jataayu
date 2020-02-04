package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.core.databinding.VerityOtpFragmentBinding
import `in`.org.projecteka.jataayu.core.handler.OtpChangeHandler
import `in`.org.projecteka.jataayu.core.handler.OtpChangeWatcher
import `in`.org.projecteka.jataayu.core.model.MessageEventType
import `in`.org.projecteka.jataayu.core.model.handler.OtpSubmissionClickHandler
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.provider.model.SuccessfulLinkingResponse
import `in`.org.projecteka.jataayu.provider.model.Token
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.org.projecteka.jataayu.util.extension.setTitle
import `in`.org.projecteka.jataayu.util.ui.UiUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class VerifyOtpFragment : BaseFragment(),
    OtpSubmissionClickHandler, OtpChangeHandler {
    override fun setButtonEnabled(isOtpEntered: Boolean) {
        binding.isOtpEntered = isOtpEntered
    }

    private lateinit var binding: VerityOtpFragmentBinding
    private val eventBus = EventBus.getDefault()

    companion object {
        fun newInstance() = VerifyOtpFragment()
    }

    private val viewModel : ProviderSearchViewModel by sharedViewModel()

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
    }

    private fun initBindings() {
        binding.selectedProviderName = viewModel.selectedProviderName
        binding.mobile = viewModel.linkAccountsResponse.value?.link?.meta?.communicationHint
        binding.clickHandler = this
        binding.isOtpEntered = false
        binding.otpChangeWatcher = OtpChangeWatcher(this)
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.verification)
    }

    private val observer = Observer<SuccessfulLinkingResponse> {
        eventBus.post(MessageEventType.ACCOUNT_LINKED)
        activity?.finish()
    }

    override fun onSubmitOtp(view: View) {
        UiUtils.hideKeyboard(activity!!)
        viewModel.successfulLinkingResponse.observe(this, observer)
        viewModel.verifyOtp(viewModel.linkAccountsResponse.value?.link?.referenceNumber!!, Token(binding.etOtp.text.toString()))
    }
}