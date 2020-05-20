package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityRecoverCmidBinding
import `in`.projecteka.jataayu.user.account.ui.fragment.ReadValuesToRecoverCmidFragment
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecoverCmidActivity : BaseActivity<ActivityRecoverCmidBinding>() {

    private val viewmodel: RecoverCmidActivityViewModel by viewModel()

    companion object {
       const val snackbarMargin = 16
    }

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
            addFragment(when(it){
                RecoverCmidActivityViewModel.Show.FIRST_SCREEN ->
                    ReadValuesToRecoverCmidFragment.newInstance()
                RecoverCmidActivityViewModel.Show.SECOND_SCREEN ->
                    ReadValuesToRecoverCmidFragment.newInstance()
//                ResetPasswordActivityViewModel.Show.THIRD_SECREEN ->
//                    ResetPasswordFragment.newInstance()

            },R.id.fragment_container)
        })
    }

    override fun layoutId(): Int =
        R.layout.activity_recover_cmid
}
