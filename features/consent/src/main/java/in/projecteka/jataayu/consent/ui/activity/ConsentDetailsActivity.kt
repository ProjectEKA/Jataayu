package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.ui.fragment.EditConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.GrantedConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.RequestedConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.RequestedConsentListFragment
import `in`.projecteka.jataayu.consent.viewmodel.ConsentDetailsActivityViewModel
import `in`.projecteka.jataayu.consent.viewmodel.RequestedConsentListViewModel
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.Success
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

class ConsentDetailsActivity : BaseActivity(), ResponseCallback {
    private val viewModel: RequestedConsentListViewModel by viewModel()
    private val detailsViewModel:   ConsentDetailsActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initObserver()
        handleIntent()
    }

    /*private fun initObserver() {
        detailsViewModel.consentEventType.observe(this, Observer {
            *//*if (it == ConsentDetailsActivityViewModel.EVENT_TYPE.CONSENT_GRANTED){
                this.setResult(501)
                this.finish()
            } else if (it == ConsentDetailsActivityViewModel.EVENT_TYPE.CONSENT_DENIED){
                this.setResult(601)
                this.finish()
            }*//*
        })
    }*/

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
                            replaceFragment(RequestedConsentDetailsFragment())
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
        if (intent.hasExtra(RequestedConsentListFragment.CONSENT_FLOW)) {
            intent.extras?.getInt(
                RequestedConsentListFragment.CONSENT_FLOW,
                ConsentFlow.REQUESTED_CONSENTS.ordinal
            )?.let { return it }
        }
        return ConsentFlow.REQUESTED_CONSENTS.ordinal
    }

    fun editConsentDetails() {
        addFragment(EditConsentDetailsFragment.newInstance())
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        showProgressBar(false)
        showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        showErrorDialog(t.localizedMessage)
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 201) {
                setPinCreated(true)
                detailsViewModel.consentEventType.value = ConsentDetailsActivityViewModel.EVENT_TYPE.USER_VERIFIED_FOR_GRAND
            }  else if (requestCode == 301){
                detailsViewModel.consentEventType.value = ConsentDetailsActivityViewModel.EVENT_TYPE.USER_VERIFIED_FOR_GRAND
            }
        }
    }*/
}