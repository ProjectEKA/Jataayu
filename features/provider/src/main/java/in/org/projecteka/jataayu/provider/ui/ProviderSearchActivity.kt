package `in`.org.projecteka.jataayu.provider.ui

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.org.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ProviderSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.provider_search_activity)
        if (savedInstanceState == null) {
            replaceFragment(ProviderSearchFragment.newInstance())
        }
        setTitle(R.string.link_provider)
    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(fragment.javaClass.name).commit()
    }

    private fun addFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack(fragment.javaClass.name).commit()
    }

    fun showPatientsAccounts() {
        addFragment(PatientAccountsFragment.newInstance())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) finish()
    }
}