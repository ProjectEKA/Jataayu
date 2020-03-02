package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.Links
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.provider.ui.handler.ConsentDetailsClickHandler
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import okhttp3.ResponseBody
import org.greenrobot.eventbus.Subscribe

class RequestedConsentDetailsFragment : ConsentDetailsFragment(), ConsentDetailsClickHandler {

    private var linkedAccounts: List<Links>? = null

    companion object {
        fun newInstance() = RequestedConsentDetailsFragment()
    }

    override fun isExpiredOrGranted(): Boolean {
        return DateTimeUtils.isDateExpired(consent.permission.dataExpiryAt)
    }

    override fun isGrantedConsent(): Boolean {
        return false
    }

    private val consentArtifactResponseObserver = Observer<PayloadResource<ConsentArtifactResponse>> {
        when (it) {
            is Loading -> {
                showProgressBar(it.isLoading)
            }
            is Success -> {
                if (it.data?.consents?.isNotEmpty() == true) {
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
            viewModel.getLinkedAccounts()
        }

        initObservers()

    }

    private fun initObservers() {
        viewModel.linkedAccountsResponse.observe(this, linkedAccountsObserver)

        viewModel.consentArtifactResponse.observe(this, consentArtifactResponseObserver)

    }

    override fun onStop() {
        super.onStop()
        eventBusInstance.unregister(this)
    }

    override fun onEditClick(view: View) {
        linkedAccounts?.let {
            eventBusInstance.postSticky(HiTypeAndLinks(hiTypeObjects, it))
            (activity as ConsentDetailsActivity).editConsentDetails()
        }
    }

    override fun onDenyConsent(view: View) {
        activity?.finish()
    }

    override fun onGrantConsent(view: View) {
        linkedAccounts?.let {
            if (it.isNotEmpty()) {
                viewModel.grantConsent(
                    consent.id,
                    viewModel.getConsentArtifact(it, hiTypeObjects, consent.permission)
                )
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandler = this
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
