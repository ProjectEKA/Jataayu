package `in`.projecteka.jataayu.ui.launcher

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.sharedPref.*
import `in`.projecteka.jataayu.util.startAccountCreation
import `in`.projecteka.jataayu.util.startDashboard
import `in`.projecteka.jataayu.util.startLogin
import `in`.projecteka.jataayu.util.startProvider
import android.os.Bundle


class LauncherActivity : BaseActivity<ActivityLauncherBinding>() {

    override fun layoutId(): Int = R.layout.activity_launcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action == UnauthorisedUserRedirectInterceptor.REDIRECT_ACTIVITY_ACTION) {
            resetCredentials()
            showLongToast("Session expired, redirecting to Login...")
        }
        redirect()
    }

    private fun redirect() {
        when {
            hasProviders() || isUserLoggedIn() ->
                startDashboard(this)
            isAccountCreated() ->
                startProvider(this)
            isUserRegistered() ->
                startAccountCreation(this)
            else -> startLogin(this)
        }
        finish()
    }

    //user should not be allowed back during redirect.
    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
