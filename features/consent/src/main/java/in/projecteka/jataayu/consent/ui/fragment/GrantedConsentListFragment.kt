package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GrantedConsentListFragment : ConsentsListFragment(){

    companion object {
        fun newInstance() = GrantedConsentListFragment()
    }
    private val grantedConsentObserver = Observer<List<Consent>?> {
        it?.let { renderConsentRequests(it, binding.spRequestFilter.selectedItemPosition) }
    }

    private val viewModel: ConsentViewModel by sharedViewModel()

    override fun getNoNewConsentsMessage(): String {
        return getString(R.string.no_granted_consents)
    }

    override fun getConsentList(): List<Consent> {
        return viewModel.grantedConsentsList.value!!
    }

    override fun getConsentFlow(): ConsentFlow {
        return ConsentFlow.GRANTED_CONSENTS
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.grantedConsentsList.observe(this, grantedConsentObserver)
    }
}