package `in`.projecteka.jataayu.ui.dashboard

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.projecteka.jataayu.databinding.ActivityDashboardBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.ui.fragment.UserAccountsFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    private val dashboardPagerAdapter by lazy { DashboardPagerAdapter(this, arrayListOf<Fragment>(
        UserAccountsFragment.newInstance(viewModel), ConsentHostFragment.newInstance())) }
    private val viewModel: DashboardViewModel by viewModel()
    override fun layoutId(): Int = R.layout.activity_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        bindViewpager()
        initObservers()
    }

    private fun bindViewpager() {
        with(binding.fragmentContainer) {
            adapter = this@DashboardActivity.dashboardPagerAdapter
            isUserInputEnabled = false
        }
    }

    private fun initObservers() {
        viewModel.showFragmentEvent.observe(this, Observer {
            when (it) {
                DashboardViewModel.Show.USER_ACCOUNT -> {
                    binding.fragmentContainer.currentItem = 0
                }
                DashboardViewModel.Show.CONSENT_HOME -> {
                    binding.fragmentContainer.currentItem = 1
                }
            }
        })
    }
}