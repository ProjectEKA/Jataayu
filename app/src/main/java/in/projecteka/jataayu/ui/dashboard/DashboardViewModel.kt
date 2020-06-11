package `in`.projecteka.jataayu.ui.dashboard

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.presentation.ui.viewmodel.BaseViewModel
import `in`.projecteka.jataayu.user.account.listener.UpdateProviderListener
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.view.MenuItem
import androidx.databinding.ObservableBoolean
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardViewModel(val preferenceRepository: PreferenceRepository) : BaseViewModel(), BottomNavigationView.OnNavigationItemSelectedListener, UpdateProviderListener {

    internal enum class Show {
        USER_ACCOUNT,
        CONSENT_HOME
    }

    var showRecords = ObservableBoolean(false)

    internal val showFragmentEvent = SingleLiveEvent<Show>()

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

    override fun updateProvider(hasProvider: Boolean) {
        showRecords.set(hasProvider)
    }

}