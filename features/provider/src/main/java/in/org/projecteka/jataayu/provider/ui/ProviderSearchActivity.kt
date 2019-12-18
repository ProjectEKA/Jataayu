package `in`.org.projecteka.jataayu.provider.ui

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.org.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ProviderSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.provider_search_activity)
        if (savedInstanceState == null) {
            replaceFragment(ProviderSearchFragment.newInstance())
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commitNow()
    }

    fun showPatientsAccounts() {
        replaceFragment(PatientAccountsFragment.newInstance())
    }

}
