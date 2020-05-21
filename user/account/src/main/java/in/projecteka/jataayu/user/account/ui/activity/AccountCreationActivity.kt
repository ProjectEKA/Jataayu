package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityCreateAccountBinding
import `in`.projecteka.jataayu.user.account.ui.fragment.ConfirmAccountFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import `in`.projecteka.jataayu.user.account.viewmodel.AccountCreationActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.ConfirmAccountViewModel
import `in`.projecteka.jataayu.util.startProvider
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountCreationActivity : BaseActivity<ActivityCreateAccountBinding>() {

    private val parentVM: AccountCreationActivityViewModel by viewModel()
    private val viewModel: ConfirmAccountViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_create_account

    companion object {
        const val KEY_ACCOUNT_CREATED = "account_created"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initToolbar()
        initObservers()
        parentVM.redirectToCreateAccountPage();
    }

    private fun initToolbar() {
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initBindings() {
        binding.viewModel = parentVM
        parentVM.appBarTitle.set(getString(R.string.create_account))
    }

    private fun initObservers() {
        parentVM.currentPage.observe(this, Observer {
            when(it){
              AccountCreationActivityViewModel.ShowPage.FIRST_SCREEN ->
                  replaceFragment(CreateAccountFragment.newInstance(), R.id.create_account_fragment_container)
              AccountCreationActivityViewModel.ShowPage.SECOND_SCREEN ->
                  replaceFragment(ConfirmAccountFragment.newInstance(), R.id.create_account_fragment_container)
            }
        })

        viewModel.createAccountResponse.observe(this,
            Observer {
                when (it) {
                    is Loading -> {parentVM.showProgress(it.isLoading)
                        println(it)}
                    is Success -> {
                        parentVM.credentialsRepository.accessToken = parentVM.getAuthTokenWithTokenType(it.data)
                        parentVM.preferenceRepository.isUserAccountCreated = true
                        startProvider(this) {
                            putExtra(KEY_ACCOUNT_CREATED, true)
                        }
                        finish()
                    }
                    is PartialFailure ->
                        showAlertDialog(getString(R.string.failure), it.error?.message, getString(android.R.string.ok))
                    is Failure ->
                        showErrorDialog(it.error.localizedMessage)

                }
            })
    }
}
