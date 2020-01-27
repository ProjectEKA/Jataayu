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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_launcher.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding
    lateinit var active: Fragment
    lateinit var accountsFragment: Fragment
    lateinit var consentFragment: Fragment
    private val eventBusInstance = EventBus.getDefault()

    private val stateChangeListener = object : View.OnAttachStateChangeListener {
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
        initFragments()
        initBindings()

        bottom_navigation.addOnAttachStateChangeListener(stateChangeListener)

        fab.setOnClickListener {
            if (!eventBusInstance.isRegistered(this))
                eventBusInstance.register(this)
            startActivity(Intent(this, ProviderSearchActivity::class.java))
        }
    }

    private fun initFragments() {
        accountsFragment = UserAccountsFragment.newInstance()
        consentFragment = ConsentHostFragment.newInstance()
        active = accountsFragment

        getFragmentTransaction(ConsentHostFragment::class.java.name, consentFragment).hide(consentFragment).commit()
        getFragmentTransaction(UserAccountsFragment::class.java.name, accountsFragment).commit()
    }

    private fun getFragmentTransaction(tag: String, fragment: Fragment): FragmentTransaction {
        return supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment, tag)
    }

    private fun initBindings() {
        binding.bottomNavListener = OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_accounts -> {
                    showAccounts()
                }
                R.id.action_consents -> {
                    showConsent()
                }
            }
            true
        }
    }

    private fun showAccounts() {
        supportFragmentManager.beginTransaction().hide(active).show(accountsFragment).commit();
        active = accountsFragment
        fab.show()
    }

    private fun showConsent() {
        fab.hide()
        supportFragmentManager.beginTransaction().hide(active).show(consentFragment).commit();
        active = consentFragment
    }

    @Subscribe
    public fun onActivityFinished(message: String) {
    }

    override fun onResume() {
        super.onResume()
        if (eventBusInstance.isRegistered(this)) {
            var snackBar = Snackbar.make(
                container,
                "${eventBusInstance.getStickyEvent(String::class.java)}",
                Snackbar.LENGTH_LONG
            )
            snackBar.show()
            eventBusInstance.removeStickyEvent(String::class.java)
            eventBusInstance.unregister(this)
        }
    }
}
