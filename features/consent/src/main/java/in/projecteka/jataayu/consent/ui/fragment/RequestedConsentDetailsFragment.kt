package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.provider.ui.handler.ConsentDetailsClickHandler
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.sharedPref.getConsentTempToken
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.Subscribe

class RequestedConsentDetailsFragment : ConsentDetailsFragment(), ConsentDetailsClickHandler {

    private var linkedAccounts: List<Links>? = null

    companion object {
        fun newInstance() = RequestedConsentDetailsFragment()
    }

    override fun isExpiredOrGrantedOrDenied(): Boolean {
        return (DateTimeUtils.isDateExpired(consent.permission.dataExpiryAt)||(consent.status == RequestStatus.DENIED))
    }

    override fun isGrantedConsent(): Boolean {
        return false
    }

    private val consentDenyObserver = Observer<PayloadResource<Void>> {
        when (it) {
            is Loading -> showProgressBar(it.isLoading, getString(R.string.denying_consent))
            is Success -> {
                activity?.let {
                    eventBusInstance.post(MessageEventType.CONSENT_DENIED)
                    activity?.finish()
                }
            }
            is PartialFailure -> {
                context?.showAlertDialog(
                    getString(R.string.failure), it.error?.message,
                    getString(android.R.string.ok)
                )
            }
            is Failure -> {
                context?.showAlertDialog(
                    getString(R.string.failure), it.error?.message,
                    getString(android.R.string.ok)
                )
            }
        }
    }

    private val consentArtifactResponseObserver = Observer<PayloadResource<ConsentArtifactResponse>> {
        when (it) {
            is Loading -> {
                showProgressBar(it.isLoading)
            }
            is Success -> {
                if (it.data?.consents?.isNotEmpty() == true) {
                    showLongToast(getString(R.string.consent_request_granted))
                    eventBusInstance.post(MessageEventType.CONSENT_GRANTED)
                    activity?.finish()
                }
            }
        }
    }

    private val linkedAccountsObserver =
        Observer<PayloadResource<LinkedAccountsResponse>> {
            when (it) {
                is Loading -> showProgressBar(it.isLoading)
                is Success -> {
                    linkedAccounts = it.data?.linkedPatient?.links
                    linkedAccounts?.forEach { link ->
                        link?.careContexts?.forEach {
                            it.contextChecked = true
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)

        if (viewModel.linkedAccountsResponse.value == null) {
            viewModel.linkedAccountsResponse.observe(this, linkedAccountsObserver)
            viewModel.getLinkedAccounts()
        }
        viewModel.consentArtifactResponse.observe(this, consentArtifactResponseObserver)
        viewModel.consentDenyResponse.observe(this, consentDenyObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onEditClick(view: View) {
        linkedAccounts?.let {
            eventBusInstance.postSticky(HiTypeAndLinks(hiTypeObjects, it))
            (activity as ConsentDetailsActivity).editConsentDetails()
        }
    }

    override fun onDenyConsent(view: View) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.title_deny_consent)
            .setPositiveButton(R.string.deny) { _, _ -> viewModel.denyConsent(consent.id) }
            .setNegativeButton(android.R.string.cancel, null)
            .setMessage(R.string.msg_deny_consent)
            .show()
    }

    override fun onGrantConsent(view: View) {
        verifyAction()
    }

    private fun verifyAction() {
        startAuthenticator()
    }

    private fun startAuthenticator() {
        (activity as ConsentDetailsActivity).validateUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandler = this
    }

    @Subscribe(sticky = true)
    public fun onConsentReceived(consent: Consent) {
        this.consent = consent
    }

    @Subscribe
    public fun onUserVerified(messageEventType: MessageEventType) {
        if (messageEventType == MessageEventType.USER_VERIFIED) {
            grant()
            eventBusInstance.unregister(this)
        }
    }

    private fun grant() {
        linkedAccounts?.let {
            if (it.isNotEmpty()) {
                if (!eventBusInstance.isRegistered(this))
                    eventBusInstance.register(this)
                showProgressBar(true)
                viewModel.grantConsent(consent.id, viewModel.getConsentArtifact(it, hiTypeObjects, consent.permission), context?.getConsentTempToken()!!)
            }
        }
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.new_request)
        renderUi()
    }
}
