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
import `in`.projecteka.jataayu.user.account.viewmodel.CreateAccountViewModel
import `in`.projecteka.jataayu.util.startProvider
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountCreationActivity : BaseActivity<ActivityCreateAccountBinding>(), AdapterView.OnItemSelectedListener {

    private val viewModel: CreateAccountViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_create_account

    companion object {
        const val KEY_ACCOUNT_CREATED = "account_created"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initToolbar()
        initObservers()
        initSpinner()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.appToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.appToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.spinnerListener = this
        binding.cgGender.setOnCheckedChangeListener(viewModel)
        viewModel.appBarTitle.set(getString(R.string.create_account))
        binding.etName.addTextChangedListener { text ->
            viewModel.validateName()
        }

        binding.etAyushmanBharatId.addTextChangedListener{text ->
            viewModel.validateAyushmanId()
        }

    }

    private fun initObservers() {
        viewModel.redirectTo.observe(this, Observer {
            addFragment(when(it){
                RedirectingActivity.ShowPage.FIRST_SCREEN ->
                    CreateAccountFragment.newInstance()
                RedirectingActivity.ShowPage.SECOND_SCREEN ->
                    ConfirmAccountFragment.newInstance()

            },R.id.create_account_fragment_container)
        })

        viewModel.createAccountResponse.observe(this,
            Observer {
                when (it) {
                    is Loading -> viewModel.showProgress(it.isLoading)
                    is Success -> {
                        viewModel.credentialsRepository.accessToken = viewModel.getAuthTokenWithTokenType(it.data)
                        viewModel.preferenceRepository.isUserAccountCreated = true
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

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, viewModel.getYearsToPopulate()
        )
        binding.spinnerYob.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spinnerYob.setSelection(0)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0) {
            viewModel.selectedYoB((view as AppCompatCheckedTextView).text.toString().toInt())
        }
    }
}
