package `in`.projecteka.jataayu.provider.ui

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import `in`.projecteka.jataayu.provider.ui.fragment.VerifyOtpFragment
import `in`.projecteka.jataayu.provider.viewmodel.ProviderActivityViewModel
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
class ProviderActivity : BaseActivity() {

    private val viewmodel: ProviderActivityViewModel by viewModel()

    companion object{
        const val KEY_ACCOUNT_CREATED = "account_created"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.getBoolean(KEY_ACCOUNT_CREATED)?.let {
            if (intent.extras?.getBoolean(KEY_ACCOUNT_CREATED)!!){
                viewmodel.showSnackbarevent.value = true
            }
        }
        replaceFragment(ProviderSearchFragment.newInstance())
    }

    fun showPatientsAccounts() {
        addFragment(PatientAccountsFragment.newInstance())
    }

    fun showVerifyOtpScreen() {
        addFragment(VerifyOtpFragment.newInstance())
    }

}
