package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.Links
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.provider.ui.handler.ConsentDetailsClickHandler
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import okhttp3.ResponseBody
import org.greenrobot.eventbus.Subscribe

class RequestedConsentDetailsFragment : ConsentDetailsFragment(), ConsentDetailsClickHandler,
    ResponseCallback {

    private lateinit var linkedAccounts: List<Links?>

    companion object {
        fun newInstance() = RequestedConsentDetailsFragment()
    }

    private val consentArtifactResponseObserver = Observer<ConsentArtifactResponse> {
        if (it.consents.isNotEmpty()) {
            eventBusInstance.post(MessageEventType.CONSENT_GRANTED)
            activity?.finish()
        }
    }

    override fun isExpiredOrGranted(): Boolean {
        return DateTimeUtils.isDateExpired(consent.permission.dataExpiryAt)
    }

    override fun isGrantedConsent(): Boolean {
        return false
    }

    private val linkedAccountsObserver =
        Observer<LinkedAccountsResponse> { linkedAccountsResponse ->
            linkedAccounts = linkedAccountsResponse.linkedPatient.links
            linkedAccounts.forEach { link ->
                link?.careContexts?.forEach {
                    it.contextChecked = true
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)

        viewModel.linkedAccountsResponse.observe(this, linkedAccountsObserver)

        if (viewModel.linkedAccountsResponse.value == null) {
            showProgressBar(true)
            viewModel.getLinkedAccounts(this)
        }

        viewModel.consentArtifactResponse.observe(this, consentArtifactResponseObserver)
    }

    override fun onStop() {
        super.onStop()
        eventBusInstance.unregister(this)
    }

    override fun onEditClick(view: View) {
        linkedAccounts.let {
            eventBusInstance.postSticky(HiTypeAndLinks(hiTypeObjects, linkedAccounts))
            (activity as ConsentDetailsActivity).editConsentDetails()
        }
    }

    override fun onDenyConsent(view: View) {
        activity?.finish()
    }

    override fun onGrantConsent(view: View) {
        if (linkedAccounts.isNotEmpty()) {
            showProgressBar(true)
            viewModel.grantConsent(
                consent.id,
                viewModel.getConsentArtifact(linkedAccounts, hiTypeObjects, consent.permission),
                this
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandler = this
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ResponseBody) {
        showProgressBar(false)
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
    }

    @Subscribe(sticky = true)
    public fun onConsentReceived(consent: Consent) {
        this.consent = consent
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.new_request)
        renderUi()
    }
}
