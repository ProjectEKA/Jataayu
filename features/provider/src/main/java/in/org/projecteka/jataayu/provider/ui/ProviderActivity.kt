package `in`.org.projecteka.jataayu.provider.ui

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.org.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.org.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import `in`.org.projecteka.jataayu.provider.ui.fragment.VerifyOtpFragment
import `in`.org.projecteka.jataayu.util.extension.findView
import android.os.Bundle

class ProviderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findView(R.id.toolbar))
        replaceFragment(ProviderSearchFragment.newInstance())
    }

    fun showPatientsAccounts() {
        addFragment(PatientAccountsFragment.newInstance())
    }

    fun showVerifyOtpScreen() {
        addFragment(VerifyOtpFragment.newInstance())
    }
}
