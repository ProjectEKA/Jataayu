package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityRecoverCmidBinding
import `in`.projecteka.jataayu.user.account.ui.fragment.DisplayCmidFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.NoMatchingRecordsFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.ReadValuesToRecoverCmidFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.RecoverCmidOtpFrgament
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel.Show.*
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
            addFragment(
                when (it) {
                    READ_VALUES_SCREEN -> ReadValuesToRecoverCmidFragment.newInstance()
                    DISPLAY_CMID_SCREEN -> DisplayCmidFragment.newInstance()
                    NO_OR_MULTIPLE_MATCHING_RECORDS -> NoMatchingRecordsFragment.newInstance()
                    OTP_SCREEN -> RecoverCmidOtpFrgament.newInstance()
                }, R.id.fragment_container
            )
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
