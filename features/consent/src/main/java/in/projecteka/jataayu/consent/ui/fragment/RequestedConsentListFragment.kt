package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RequestedConsentListFragment : ConsentsListFragment() {

    companion object {
        fun newInstance() = RequestedConsentListFragment()
    }

    private val requestedConsentObserver = Observer<List<Consent>?> {
        it?.let { renderConsentRequests(it, binding.spRequestFilter.selectedItemPosition) }
    }

    private val viewModel: ConsentViewModel by sharedViewModel()

    override fun getNoNewConsentsMessage(): String {
        return getString(R.string.no_new_consent_requests)
    }

    override fun getConsentList(): List<Consent> {
        return viewModel.requestedConsentsList.value!!
    }

    override fun getConsentFlow(): ConsentFlow {
        return ConsentFlow.REQUESTED_CONSENTS
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestedConsentsList.observe(this, requestedConsentObserver)
    }
}