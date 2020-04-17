package `in`.projecteka.jataayu.ui.dashboard

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardViewModel : BaseViewModel(), BottomNavigationView.OnNavigationItemSelectedListener {

    internal enum class Show {
        USER_ACCOUNT,
        CONSENT_HOME
    }

    internal val showFragmentEvent = SingleLiveEvent<Show>()

    fun setup() {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_accounts -> {
            showFragmentEvent.value = Show.USER_ACCOUNT
            true
        }
        R.id.action_consents -> {
            showFragmentEvent.value = Show.CONSENT_HOME
            true
        }
        else -> false
    }

}