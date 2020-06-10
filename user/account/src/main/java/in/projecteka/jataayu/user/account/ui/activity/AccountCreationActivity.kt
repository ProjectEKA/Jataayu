package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityCreateAccountBinding
import `in`.projecteka.jataayu.user.account.ui.fragment.ConfirmAccountFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.SuccessPageFragment
import `in`.projecteka.jataayu.user.account.viewmodel.AccountCreationActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.AccountCreationActivityViewModel.ShowPage.*
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountCreationActivity : BaseActivity<ActivityCreateAccountBinding>() {

    private val viewModel: AccountCreationActivityViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_create_account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initObservers()
        viewModel.redirectToCreateAccountPage()
    }

    override fun onBackPressed() {
        if (viewModel.currentPage.value != SUCCESS_SCREEN) {
            super.onBackPressed()
        }
    }

    private fun initBindings() {
        initToolbar()
        binding.viewModel = viewModel
    }

    private fun initToolbar() {
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initObservers() {
        viewModel.currentPage.observe(this, Observer {
            when(it){
              ACCOUNT_INFO_SCREEN ->
                  addFragment(CreateAccountFragment.newInstance(), R.id.create_account_fragment_container)
              CONFIRM_ACCOUNT_SCREEN ->
                  addFragment(ConfirmAccountFragment.newInstance(), R.id.create_account_fragment_container)
                SUCCESS_SCREEN -> {
                    replaceFragment(SuccessPageFragment.newInstance(), R.id.create_account_fragment_container, false)
                    // do not show back button
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        })
    }
}
