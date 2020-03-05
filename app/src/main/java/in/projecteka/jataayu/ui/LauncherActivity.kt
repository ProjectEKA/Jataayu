package `in`.projecteka.jataayu.ui

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.account.AccountCreationActivity
import `in`.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.core.model.ProviderAddedEvent
import `in`.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.projecteka.jataayu.module.networkModule
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.provider.ui.ProviderActivity
import `in`.projecteka.jataayu.registration.ui.activity.LoginActivity
import `in`.projecteka.jataayu.registration.ui.activity.RegistrationActivity
import `in`.projecteka.jataayu.ui.LauncherActivity.RequestCodes.*
import `in`.projecteka.jataayu.user.account.ui.fragment.UserAccountsFragment
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.extension.startActivity
import `in`.projecteka.jataayu.util.extension.startActivityForResult
import `in`.projecteka.jataayu.util.sharedPref.getBoolean
import `in`.projecteka.jataayu.util.sharedPref.putBoolean
import android.app.Activity
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
import kotlinx.android.synthetic.main.activity_launcher.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules


class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding
    private lateinit var active: Fragment
    private lateinit var accountsFragment: UserAccountsFragment
    private lateinit var consentFragment: ConsentHostFragment
    private val eventBusInstance = EventBus.getDefault()

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

    companion object {
        const val LOGGED_IN = "logged_in"
        const val REGISTERED = "registered"
        const val ACCOUNT_CREATED = "account_created"
        const val PROVIDER_ADDED = "provider_added"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        redirectIfNeeded()
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

    private fun redirectIfNeeded() {
        when {
            getBoolean(PROVIDER_ADDED, false) || getBoolean(LOGGED_IN, false) -> {
                binding = DataBindingUtil.setContentView(
                    this,
                    R.layout.activity_launcher
                )

                initFragments()
                initBindings()
                initUi()
            }
            getBoolean(ACCOUNT_CREATED, false) -> {
                startActivityForResult(ProviderActivity::class.java, ADD_PROVIDER.ordinal)
            }
            getBoolean(REGISTERED, false) -> {
                startActivityForResult(AccountCreationActivity::class.java, RequestCodes.CREATE_ACCOUNT.ordinal)
            }
            else -> {
                startActivityForResult(LoginActivity::class.java, LOGIN.ordinal)
            }
        }
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public fun onEvent(messageEventType: MessageEventType) {
        when (messageEventType) {
            MessageEventType.CONSENT_GRANTED -> {
                runOnUiThread{ showSnackbar(getString(R.string.consent_granted)) }
                eventBusInstance.post(MessageEventType.SELECT_CONSENTS_TAB)
            }
            MessageEventType.ACCOUNT_LINKED -> {
                runOnUiThread{ showSnackbar(getString(R.string.account_linked_successfully)) }
                eventBusInstance.post(ProviderAddedEvent.PROVIDER_ADDED)
            }
            MessageEventType.ACCOUNT_CREATED -> {
                runOnUiThread{ showLongToast(getString(R.string.registered_successfully)) }
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

    override fun onDestroy() {
        eventBusInstance.unregister(this)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        unloadKoinModules(listOf(networkModule))
        loadKoinModules(listOf(networkModule))
        when (requestCode) {
            LOGIN.ordinal -> {
                startFlowIfRequired(resultCode, LOGGED_IN)
            }
            REGISTER.ordinal -> startFlowIfRequired(resultCode, REGISTERED)
            CREATE_ACCOUNT.ordinal -> {
                startFlowIfRequired(resultCode, ACCOUNT_CREATED)
                eventBusInstance.post(MessageEventType.ACCOUNT_CREATED)
            }
            ADD_PROVIDER.ordinal -> startFlowIfRequired(resultCode, PROVIDER_ADDED)
        }
    }

    private fun startFlowIfRequired(resultCode: Int, key: String) {
        when (resultCode) {
            Activity.RESULT_CANCELED -> {
                finish()
            }
            Activity.RESULT_FIRST_USER -> {
                startActivityForResult(RegistrationActivity::class.java, REGISTER.ordinal)
            }
            else -> {
                putBoolean(key, true)
                redirectIfNeeded()
            }
        }
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
}
