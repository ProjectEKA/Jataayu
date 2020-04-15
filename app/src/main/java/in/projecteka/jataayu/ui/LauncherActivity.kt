package `in`.projecteka.jataayu.ui

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.core.model.ProviderAddedEvent
import `in`.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.provider.ui.ProviderActivity
import `in`.projecteka.jataayu.user.account.ui.fragment.UserAccountsFragment
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.extension.startActivity
import `in`.projecteka.jataayu.util.sharedPref.*
import `in`.projecteka.jataayu.util.startAccountCreation
import `in`.projecteka.jataayu.util.startLogin
import `in`.projecteka.jataayu.util.startProvider
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
import kotlinx.android.synthetic.main.activity_launcher.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding
    private lateinit var active: Fragment
    private lateinit var accountsFragment: UserAccountsFragment
    private lateinit var consentFragment: ConsentHostFragment

    override fun onStart() {
        super.onStart()
        if(!eventBusInstance.isRegistered(this)){
            eventBusInstance.register(this)
        }
    }

    override fun onDestroy() {
        if(eventBusInstance.isRegistered(this)){
            eventBusInstance.unregister(this)
        }
        super.onDestroy()
    }

    private val stateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {

        }

        override fun onViewAttachedToWindow(v: View?) {
            bottom_navigation.selectedItemId = bottom_navigation.menu.getItem(0).itemId
        }
    }

    enum class RequestCodes {
        REGISTER, CREATE_ACCOUNT, ADD_PROVIDER, LOGIN
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.action == UnauthorisedUserRedirectInterceptor.REDIRECT_ACTIVITY_ACTION){
            resetCredentials()
            showLongToast("Session expired, redirecting to Login...")
        }
        redirectIfNeeded()
    }

    private fun initFragments() {
        accountsFragment = UserAccountsFragment.newInstance()
        consentFragment = ConsentHostFragment.newInstance()
        active = accountsFragment

        getFragmentTransaction(ConsentHostFragment::class.java.name, consentFragment).hide(consentFragment).commit()
        getFragmentTransaction(UserAccountsFragment::class.java.name, accountsFragment).commit()
    }

    private fun redirectIfNeeded() {

        when{
            hasProviders() || isUserLoggedIn() -> {
                loadAccountsFragment()
            }
            isAccountCreated() -> {
                startProvider(this)
                finish()
            }
            isUserRegistered() -> {
                startAccountCreation(this)
                finish()
            }
            else -> {
                startLogin(this)
                finish()
            }
        }
    }

    private fun loadAccountsFragment() {
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_launcher
        )
        initFragments()
        initBindings()
        initUi()
    }

    private fun initUi() {
        bottom_navigation.addOnAttachStateChangeListener(stateChangeListener)

        fab.setOnClickListener {
            startActivity(ProviderActivity::class.java)
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(messageEventType: MessageEventType) {
        when (messageEventType) {
            MessageEventType.CONSENT_GRANTED -> {
               showSnackbar(getString(R.string.consent_granted))
                eventBusInstance.post(MessageEventType.SELECT_CONSENTS_TAB)
            }
            MessageEventType.ACCOUNT_LINKED -> {
                showSnackbar(getString(R.string.account_linked_successfully))
                eventBusInstance.post(ProviderAddedEvent.PROVIDER_ADDED)
            }
            MessageEventType.ACCOUNT_CREATED -> {
                showLongToast(getString(R.string.registered_successfully))
            }
            MessageEventType.CONSENT_DENIED -> {
                showSnackbar(getString(R.string.consent_denied))
            }
            MessageEventType.CONSENT_REVOKED -> {
                showSnackbar(getString(R.string.consent_revoked))
            }
        }
    }

    private fun showSnackbar(message: String) {
        val spannableString = SpannableString(message)

        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val snackbar = Snackbar.make(fragment_container, spannableString, 2000)
        snackbar.anchorView = bottom_navigation
        snackbar.show()
    }
    override fun showProgressBar(shouldShow: Boolean) {
        showProgressBar(shouldShow, "")
    }

    override fun showProgressBar(shouldShow: Boolean, message: String) {
        binding.progressBarVisibility = shouldShow
        binding.progressBarMessage = message
    }

    override fun setProgressBarVisibilityValue(shouldShow: Boolean) {
        binding.progressBarVisibility = shouldShow
    }

    override fun setProgressBarMessage(message: String) {
        binding.progressBarMessage = message
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        unloadKoinModules(listOf(networkModule))
        loadKoinModules(listOf(networkModule))

        if (resultCode == Activity.RESULT_OK && requestCode == LOGIN.ordinal)
                loadAccountsFragment()
        }*/
}
