package `in`.org.projecteka.jataayu.consent.ui.activity

import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentDetailsFragment
import `in`.org.projecteka.jataayu.consent.ui.fragment.EditConsentDetailsFragment
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import android.os.Bundle

class ConsentDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(ConsentDetailsFragment.newInstance())
    }

    fun editConsentDetails(){
        addFragment(EditConsentDetailsFragment.newInstance())
    }
}