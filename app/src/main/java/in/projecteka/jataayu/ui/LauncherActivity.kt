package `in`.projecteka.jataayu.ui

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.provider.ui.ProviderActivity
import `in`.projecteka.jataayu.user.account.ui.fragment.UserAccountsFragment
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.extension.startActivity
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
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_launcher.*
import org.koin.androidx.viewmodel.ext.android.viewModel
class LauncherActivity : BaseActivity() {
    private lateinit var binding: ActivityLauncherBinding
    private lateinit var active: Fragment
    private lateinit var accountsFragment: UserAccountsFragment
    private lateinit var consentFragment: ConsentHostFragment

    private val viewModel: LauncherViewModel by viewModel()

    private val stateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {

        }

        override fun onViewAttachedToWindow(v: View?) {
            bottom_navigation.selectedItemId = bottom_navigation.menu.getItem(0).itemId
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action == UnauthorisedUserRedirectInterceptor.REDIRECT_ACTIVITY_ACTION) {
            viewModel.resetCredentials()
            showLongToast("Session expired, redirecting to Login...")
        }
        viewModel.redirectIfNeeded()
        initObservers()
    }

    private fun initObservers(){
        viewModel.startLogin.observe(this, Observer {
            startLogin(this)
            finish()
        })
        viewModel.startAccountFragments.observe(this, Observer {
            loadAccountsFragment()
        })
        viewModel.startProvider.observe(this, Observer {
            startProvider(this)
            finish()
        })
        viewModel.startAccountCreation.observe(this, Observer {
            startAccountCreation(this)
            finish()
        })
    }

    private fun initFragments() {
        accountsFragment = UserAccountsFragment.newInstance()
        consentFragment = ConsentHostFragment.newInstance()
        active = accountsFragment

        getFragmentTransaction(ConsentHostFragment::class.java.name, consentFragment).hide(consentFragment).commit()
        getFragmentTransaction(UserAccountsFragment::class.java.name, accountsFragment).commit()
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

}
