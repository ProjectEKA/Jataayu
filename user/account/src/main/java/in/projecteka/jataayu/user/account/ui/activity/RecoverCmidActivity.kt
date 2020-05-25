package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityRecoverCmidBinding
import `in`.projecteka.jataayu.user.account.ui.fragment.DisplayCmidFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.NoMatchingRecordsFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.ReadValuesToRecoverCmidFragment
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecoverCmidActivity : BaseActivity<ActivityRecoverCmidBinding>() {

    private val viewmodel: RecoverCmidActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initObservers()
        viewmodel.init()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initObservers() {
        viewmodel.redirectTo.observe(this, Observer {

            when(it){
                RecoverCmidActivityViewModel.Show.READ_VALUES_SCREEN -> {
                    addFragment(
                        ReadValuesToRecoverCmidFragment.newInstance(),
                        R.id.fragment_container
                    )
                }
                RecoverCmidActivityViewModel.Show.DISPLAY_CMID_SCREEN -> {
                    addFragment(DisplayCmidFragment.newInstance(),R.id.fragment_container)
                }
                RecoverCmidActivityViewModel.Show.NO_OR_MULTIPLE_MATCHING_RECORDS -> {
                    addFragment(NoMatchingRecordsFragment.newInstance(),R.id.fragment_container)
                }

            }
        })
    }

    fun updateActionbar(show: Boolean){
        if (show){
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    override fun layoutId(): Int =
        R.layout.activity_recover_cmid
}
