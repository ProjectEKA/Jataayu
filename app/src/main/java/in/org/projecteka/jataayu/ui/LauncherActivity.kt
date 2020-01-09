package `in`.org.projecteka.jataayu.ui

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.org.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.org.projecteka.jataayu.provider.ui.ProviderSearchActivity
import `in`.org.projecteka.jataayu.user.account.ui.fragment.UserAccountsFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_launcher.*


class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding

    val stateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {

        }

        override fun onViewAttachedToWindow(v: View?) {
            bottom_navigation.selectedItemId = bottom_navigation.menu.getItem(0).itemId
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_launcher
        )

        initBindings()

        bottom_navigation.addOnAttachStateChangeListener(stateChangeListener)

        fab.setOnClickListener {
            startActivity(Intent(this, ProviderSearchActivity::class.java))
        }
    }

    private fun initBindings() {
        binding.bottomNavListener = OnNavigationItemSelectedListener { menuItem ->
            when {
                menuItem.itemId == R.id.action_consents -> showConsent()
                menuItem.itemId == R.id.action_accounts -> showAccounts()
            }
            true
        }
    }

    override fun replaceFragment(fragment: Fragment) {
        val backStateName = fragment.javaClass.name

        val manager = supportFragmentManager
        val popped = manager.popBackStackImmediate(backStateName, 0)

        if (!popped && manager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
            manager.beginTransaction()
            .replace(R.id.fragment_container, fragment, backStateName)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
            manager.executePendingTransactions()
        }
    }

    private fun showAccounts() {
        fab.show()
        replaceFragment(UserAccountsFragment.newInstance())
    }

    private fun showConsent() {
        fab.hide()
        replaceFragment(ConsentHostFragment.newInstance())
    }
}
