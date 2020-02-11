package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.ui.fragment.ConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.EditConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.GrantedConsentDetailsFragment
import `in`.projecteka.jataayu.consent.ui.fragment.RequestListFragment
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import android.os.Bundle

class ConsentDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(getNextFragment())
    }

    private fun getNextFragment(): ConsentDetailsFragment {
        return if (getFlowType() == ConsentFlow.REQUESTED_CONSENTS.ordinal)
            ConsentDetailsFragment.newInstance()
        else GrantedConsentDetailsFragment.newInstance()
    }

    private fun getFlowType(): Int {
        if (intent.hasExtra(RequestListFragment.CONSENT_FLOW)) {
            intent.extras?.getInt(
                RequestListFragment.CONSENT_FLOW,
                ConsentFlow.REQUESTED_CONSENTS.ordinal
            )?.let { return it }
        }
        return ConsentFlow.REQUESTED_CONSENTS.ordinal
    }

    fun editConsentDetails() {
        addFragment(EditConsentDetailsFragment.newInstance())
    }
}