package `in`.org.projecteka.jataayu.ui

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.org.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.org.projecteka.jataayu.provider.ui.ProviderSearchActivity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLauncherBinding>(
            this,
            R.layout.activity_launcher
        )

        initBindings()

        fab.setOnClickListener {
            startActivity(Intent(this, ProviderSearchActivity::class.java))
        }
    }

    private fun initBindings() {

        binding.bottomNavListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
                if (menuItem.itemId == R.id.action_consents) {
                    showConsent()
                } else if (menuItem.itemId == R.id.action_consents) {
                    showAccounts()
                }
                true
            }
    }

    protected override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(fragment.javaClass.name).commit()
    }

    protected override fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
            .addToBackStack(fragment.javaClass.name).commit()
    }

    private fun showAccounts() {
//        replaceFragment(UserAccountsFragment.newInstance())
    }

    private fun showConsent() {
        replaceFragment(ConsentHostFragment.newInstance())

    }
}
