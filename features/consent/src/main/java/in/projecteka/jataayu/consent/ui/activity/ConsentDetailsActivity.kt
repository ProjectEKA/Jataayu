package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.ui.fragment.EditConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.GrantedConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.RequestedConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.RequestedListFragment
import `in`.projecteka.jataayu.consent.viewmodel.RequestedListViewModel
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConsentDetailsActivity : BaseActivity<BaseActivityBinding>(), ResponseCallback {

    override fun layoutId(): Int = R.layout.base_activity

    private val viewModel: RequestedListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent()
        initToolbar()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        val appLinkData = intent.data
        if (appLinkData == null) {
            addFragment(getDetailsFragment(), R.id.fragment_container)
        } else {
            deepLinkConsent(appLinkData)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.baseToolbar.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.baseToolbar.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun deepLinkConsent(appLinkData: Uri) {
        viewModel.showProgress(true, R.string.loading_requests)
        val consentId = appLinkData.lastPathSegment
        if (consentId.isNullOrBlank()) {
            finish()
        }
        consentId?.let { id ->
            viewModel.consentListResponse.observe(this, Observer<PayloadResource<ConsentsListResponse>> { payload ->
                when (payload) {
                    is Success -> {
                        payload.data?.requests?.find { it.id == consentId }?.let {
                            EventBus.getDefault().postSticky(it)
                            replaceFragment(RequestedConsentDetailsFragment(), R.id.fragment_container)
                        }
                    }
                }

            })
            viewModel.getConsents(offset = 0)
        }
    }

    private fun getDetailsFragment(): BaseFragment {
        return if (getFlowType() == ConsentFlow.REQUESTED_CONSENTS.ordinal)
            RequestedConsentDetailsFragment.newInstance()
        else GrantedConsentDetailsFragment.newInstance()
    }

    private fun getFlowType(): Int {
        if (intent.hasExtra(RequestedListFragment.CONSENT_FLOW)) {
            intent.extras?.getInt(
                RequestedListFragment.CONSENT_FLOW,
                ConsentFlow.REQUESTED_CONSENTS.ordinal
            )?.let { return it }
        }
        return ConsentFlow.REQUESTED_CONSENTS.ordinal
    }

    fun editConsentDetails() {
        addFragment(EditConsentDetailsFragment.newInstance(), R.id.fragment_container)
    }

   fun updateTitle(title: String) {
       binding.title = title
   }

    override fun <T> onSuccess(body: T?) {
        viewModel.showProgress(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        viewModel.showProgress(false)
        showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        viewModel.showProgress(false)
        showErrorDialog(t.localizedMessage)
    }
}