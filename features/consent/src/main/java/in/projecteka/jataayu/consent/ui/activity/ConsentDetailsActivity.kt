package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.projecteka.jataayu.consent.ui.fragment.*
import `in`.projecteka.jataayu.consent.viewmodel.RequestedConsentViewModel
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.extension.startActivityForResult
import `in`.projecteka.jataayu.util.sharedPref.getPinCreated
import `in`.projecteka.jataayu.util.sharedPref.setPinCreated
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConsentDetailsActivity : BaseActivity<BaseActivityBinding>(), ResponseCallback {

    override fun layoutId(): Int = R.layout.base_activity

    private val viewModel: RequestedConsentViewModel by viewModel()

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
            addFragment(getDetailsFragment(), R.id.fragment_container)
        } else {
            deepLinkConsent(appLinkData)
        }
    }

    private fun deepLinkConsent(appLinkData: Uri) {
//        showProgressBar(true, getString(R.string.loading_requests))
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
            viewModel.getConsents()
        }
    }

    private fun getDetailsFragment(): BaseFragment {
        return if (getFlowType() == ConsentFlow.REQUESTED_CONSENTS.ordinal)
            RequestedConsentDetailsFragment.newInstance()
        else GrantedConsentDetailsFragment.newInstance()
    }

    private fun getFlowType(): Int {
        if (intent.hasExtra(RequestedFragment.CONSENT_FLOW)) {
            intent.extras?.getInt(
                RequestedFragment.CONSENT_FLOW,
                ConsentFlow.REQUESTED_CONSENTS.ordinal
            )?.let { return it }
        }
        return ConsentFlow.REQUESTED_CONSENTS.ordinal
    }

    fun editConsentDetails() {
        addFragment(EditConsentDetailsFragment.newInstance(), R.id.fragment_container)
    }

    override fun <T> onSuccess(body: T?) {
//        showProgressBar(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
//        showProgressBar(false)
        showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
//        showProgressBar(false)
        showErrorDialog(t.localizedMessage)
    }

    fun validateUser() {
        if (getPinCreated()) {
            addFragment(UserVerificationFragment.newInstance(), R.id.fragment_container)
        } else {
            startActivityForResult(CreatePinActivity::class.java, 201)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            setPinCreated(true)
            EventBus.getDefault().post(MessageEventType.USER_VERIFIED)
        }
    }
}