package `in`.org.projecteka.jataayu.ui

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.org.projecteka.jataayu.core.model.MessageEventType
import `in`.org.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.org.projecteka.jataayu.provider.ui.ProviderSearchActivity
import `in`.org.projecteka.jataayu.user.account.ui.fragment.UserAccountsFragment
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_launcher.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding
    private lateinit var active: Fragment
    private lateinit var accountsFragment: Fragment
    private lateinit var consentFragment: Fragment
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
            startActivity(Intent(this, ProviderSearchActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public fun onEvent(messageEventType: MessageEventType) {
        when (messageEventType) {
            MessageEventType.CONSENT_GRANTED -> {
                showSnackbar(getString(R.string.consent_granted))
            }
            MessageEventType.ACCOUNT_LINKED ->
                showSnackbar(getString(R.string.account_linked_successfully))
        }
    }

    private fun showSnackbar(message: String) {
        val spannableString = SpannableString(message)
        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val snackbar = Snackbar.make(fragment_container, spannableString, LENGTH_LONG)
        snackbar.anchorView = bottom_navigation
        snackbar.show()
    }

    override fun onDestroy() {
        eventBusInstance.unregister(this)
        super.onDestroy()
    }
}
