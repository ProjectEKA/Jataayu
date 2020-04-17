package `in`.projecteka.jataayu.provider.ui

import `in`.projecteka.featuresprovider.R
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import `in`.projecteka.jataayu.provider.ui.fragment.VerifyOtpFragment
import android.os.Bundle

class ProviderActivity : BaseActivity<BaseActivityBinding>() {

    override fun layoutId(): Int = R.layout.base_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(ProviderSearchFragment.newInstance(),R.id.fragment_container)
    }

    fun showPatientsAccounts() {
        addFragment(PatientAccountsFragment.newInstance(),R.id.fragment_container)
    }

    fun showVerifyOtpScreen() {
        addFragment(VerifyOtpFragment.newInstance(),R.id.fragment_container)
    }
}
