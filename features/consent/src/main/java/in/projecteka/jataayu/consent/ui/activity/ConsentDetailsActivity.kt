package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.ui.fragment.ConsentsListFragment
import `in`.projecteka.jataayu.consent.ui.fragment.EditConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.GrantedConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.RequestedConsentDetailsFragment
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConsentDetailsActivity : BaseActivity() {
    private val viewModel: ConsentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        val appLinkData = intent.data
        if (appLinkData == null) {
            addFragment(getDetailsFragment())
        } else {
            deepLinkConsent(appLinkData)
        }
    }

    private fun deepLinkConsent(appLinkData: Uri) {
        showProgressBar(true, getString(R.string.loading_requests))
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
                            replaceFragment(RequestedConsentDetailsFragment.newInstance())
                        }
                    }
                }

            })
            viewModel.getConsents()
        }
    }

    private fun getDetailsFragment(): BaseFragment {
        return if (getFlowType() == ConsentFlow.REQUESTED_CONSENTS.ordinal)
            RequestedConsentDetailsFragment.newInstance()
        else GrantedConsentDetailsFragment.newInstance()
    }

    private fun getFlowType(): Int {
        if (intent.hasExtra(ConsentsListFragment.CONSENT_FLOW)) {
            intent.extras?.getInt(
                ConsentsListFragment.CONSENT_FLOW,
                ConsentFlow.REQUESTED_CONSENTS.ordinal
            )?.let { return it }
        }
        return ConsentFlow.REQUESTED_CONSENTS.ordinal
    }

    fun editConsentDetails() {
        addFragment(EditConsentDetailsFragment.newInstance())
    }
}