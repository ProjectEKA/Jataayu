package `in`.org.projecteka.jataayu.consent.ui.activity

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.ActivityConsentDetailsBinding
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConfirmConsentFragment
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentDetailsFragment
import `in`.org.projecteka.jataayu.consent.ui.fragment.EditConsentDetailsFragment
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_consent_details.*

class ConsentDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityConsentDetailsBinding>(this, R.layout.activity_consent_details)
        addFragment(ConsentDetailsFragment.newInstance())
        setSupportActionBar(toolbar)
    }

    fun editConsentDetails(){
        addFragment(EditConsentDetailsFragment.newInstance())
    }

    fun grantRequest(){
        addFragment(ConfirmConsentFragment.newInstance())
    }
}