package `in`.projecteka.jataayu.provider.ui

import `in`.projecteka.featuresprovider.R
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import `in`.projecteka.jataayu.provider.ui.fragment.VerifyOtpFragment
import `in`.projecteka.jataayu.provider.viewmodel.ProviderActivityViewModel
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProviderActivity : BaseActivity<BaseActivityBinding>() {

    override fun layoutId(): Int = R.layout.base_activity

    private val viewmodel: ProviderActivityViewModel by viewModel()

    companion object{
        const val KEY_ACCOUNT_CREATED = "account_created"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(ProviderSearchFragment.newInstance(),R.id.fragment_container)
        intent.extras?.getBoolean(KEY_ACCOUNT_CREATED)?.let {
            if (intent.extras?.getBoolean(KEY_ACCOUNT_CREATED)!!){
                viewmodel.showSnackbarevent.value = true
            }
        }
    }

    fun showPatientsAccounts() {
        addFragment(PatientAccountsFragment.newInstance(),R.id.fragment_container)
    }

    fun showVerifyOtpScreen() {
        addFragment(VerifyOtpFragment.newInstance(),R.id.fragment_container)
    }

}
