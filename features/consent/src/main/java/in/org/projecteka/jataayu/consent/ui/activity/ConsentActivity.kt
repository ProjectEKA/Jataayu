package `in`.org.projecteka.jataayu.consent.ui.activity

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.ActivityConsentBinding
import `in`.org.projecteka.jataayu.consent.ui.adapter.SectionsPagerAdapter
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

class ConsentActivity : BaseActivity() {
    private lateinit var binding: ActivityConsentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityConsentBinding>(this,
            R.layout.activity_consent
        )
        setTitle(R.string.title_activity_consent)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        tabs.setupWithViewPager(viewPager)

        
    }
}