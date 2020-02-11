package `in`.projecteka.jataayu.provider.ui

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import `in`.projecteka.jataayu.provider.ui.fragment.VerifyOtpFragment
import android.os.Bundle

class ProviderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(ProviderSearchFragment.newInstance())
    }

    fun showPatientsAccounts() {
        addFragment(PatientAccountsFragment.newInstance())
    }

    fun showVerifyOtpScreen() {
        addFragment(VerifyOtpFragment.newInstance())
    }
}
