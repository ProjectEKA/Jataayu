package `in`.org.projecteka.jataayu.consent.ui.activity

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.ActivityConsentDetailsBinding
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_consent_details.*

class ConsentDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityConsentDetailsBinding>(this, R.layout.activity_consent_details)
        setSupportActionBar(toolbar)
        setTitle(R.string.new_request)
        replaceFragment(ConsentDetailsFragment.newInstance())
    }
}
