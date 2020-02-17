package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val INDEX_ACTIVE = 0
private const val INDEX_EXPIRED = 1
private const val INDEX_ALL = 2

class GrantedConsentListFragment : RequestedConsentsListFragment() {

    companion object {
        fun newInstance() = GrantedConsentListFragment()
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
}