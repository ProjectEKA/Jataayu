package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityCreateAccountBinding
import `in`.projecteka.jataayu.user.account.ui.fragment.ConfirmAccountFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import `in`.projecteka.jataayu.user.account.viewmodel.AccountCreationActivityViewModel
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountCreationActivity : BaseActivity<ActivityCreateAccountBinding>() {

    private val viewModel: AccountCreationActivityViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_create_account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initToolbar()
        initObservers()
        viewModel.redirectToCreateAccountPage();
    }

    private fun initToolbar() {
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        viewModel.appBarTitle.set(getString(R.string.create_account))
    }

    private fun initObservers() {
        viewModel.currentPage.observe(this, Observer {
            when(it){
              AccountCreationActivityViewModel.ShowPage.FIRST_SCREEN ->
                  replaceFragment(CreateAccountFragment.newInstance(), R.id.create_account_fragment_container)
              AccountCreationActivityViewModel.ShowPage.SECOND_SCREEN ->
                  replaceFragment(ConfirmAccountFragment.newInstance(), R.id.create_account_fragment_container)
            }
        })
    }
}
