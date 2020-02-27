package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.Links
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.consent_details_fragment.*
import okhttp3.ResponseBody
import org.greenrobot.eventbus.Subscribe

class GrantedConsentDetailsFragment : ConsentDetailsFragment(), ResponseCallback {

    private lateinit var consentId: String
    private lateinit var genericRecyclerViewAdapter: GenericRecyclerViewAdapter
    private lateinit var linkedAccounts: List<Links?>
    private lateinit var linkedAccountsAndCount : Pair<List<IDataBindingModel>, Int>

    private val grantedConsentDetailsObserver =
        Observer<List<GrantedConsentDetailsResponse>> { grantedConsents ->
            this.consent = grantedConsents[0].consentDetail
            renderUi()
            populateLinkedAccounts(grantedConsents)
        }

    private val linkedAccountsObserver = Observer<LinkedAccountsResponse> { linkedAccountsResponse ->
            linkedAccounts = linkedAccountsResponse.linkedPatient.links
            viewModel.grantedConsentDetailsResponse.observe(this, grantedConsentDetailsObserver)

            if (viewModel.grantedConsentDetailsResponse.value == null) {
                showProgressBar(true)
                viewModel.getGrantedConsentDetails(consentId, this)
            }
        }

    private fun populateLinkedAccounts(grantedConsents: List<GrantedConsentDetailsResponse>) {
        linkedAccountsAndCount= viewModel.getItems(grantedConsents, linkedAccounts)
        genericRecyclerViewAdapter =
            GenericRecyclerViewAdapter(this@GrantedConsentDetailsFragment, linkedAccountsAndCount.first)
        rvLinkedAccounts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = genericRecyclerViewAdapter
            val dividerItemDecorator =
                DividerItemDecorator(ContextCompat.getDrawable(context!!, R.color.transparent)!!)
            addItemDecoration(dividerItemDecorator)
        }

        binding.tvProviders.text = String.format(context!!.getString(R.string.all_linked_providers_with_count), linkedAccountsAndCount.second)
    }

    companion object {
        fun newInstance() = GrantedConsentDetailsFragment()
    }

    override fun onStop() {
        super.onStop()
        eventBusInstance.unregister(this)
    }

    override fun isExpiredOrGranted(): Boolean {
        return true
    }

    override fun isGrantedConsent(): Boolean {
        return true
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
    public fun onConsentIdReceived(consentId: String) {
        this.consentId = consentId
    }
}
