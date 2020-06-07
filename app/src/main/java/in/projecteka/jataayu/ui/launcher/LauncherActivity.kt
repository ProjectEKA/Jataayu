package `in`.projecteka.jataayu.ui.launcher

import `in`.projecteka.jataayu.network.interceptor.UnauthorisedUserRedirectInterceptor
import `in`.projecteka.jataayu.ui.LauncherViewModel
import `in`.projecteka.jataayu.util.*
import `in`.projecteka.jataayu.util.extension.showLongToast
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel


class LauncherActivity : AppCompatActivity() {

    private val viewModel: LauncherViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action == UnauthorisedUserRedirectInterceptor.REDIRECT_ACTIVITY_ACTION) {
            viewModel.resetCredentials()
            showLongToast("Session expired, redirecting to Login...")
        }
        viewModel.redirectIfNeeded()
        initObservers()
    }

    private fun initObservers() {
        viewModel.startLogin.observe(this, Observer {
            startLogin(this)
            finish()
        })
        viewModel.startDashboard.observe(this, Observer {
            startDashboard(this)
            finish()
        })
        viewModel.startProvider.observe(this, Observer {
            startProvider(this)
            finish()
        })
        viewModel.startAccountCreation.observe(this, Observer {
            startAccountCreation(this)
            finish()
        })
        viewModel.startIntroductionScreens.observe(this, Observer {
            startIntroScreens(this)
            finish()
        })
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}
