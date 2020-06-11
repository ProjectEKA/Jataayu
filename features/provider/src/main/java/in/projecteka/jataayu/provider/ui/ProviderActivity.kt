package `in`.projecteka.jataayu.provider.ui

import `in`.projecteka.featuresprovider.R
import `in`.projecteka.jataayu.core.ProviderLinkType
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.activity.BaseActivity
import `in`.projecteka.jataayu.provider.ui.fragment.PatientAccountsFragment
import `in`.projecteka.jataayu.provider.ui.fragment.ProviderSearchFragment
import `in`.projecteka.jataayu.provider.ui.fragment.VerifyOtpFragment
import `in`.projecteka.jataayu.provider.viewmodel.ProviderActivityViewModel
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProviderActivity : BaseActivity<BaseActivityBinding>() {

    override fun layoutId(): Int = R.layout.base_activity

    private val viewmodel: ProviderActivityViewModel by viewModel()

    companion object{
        const val KEY_ACCOUNT_CREATED = "account_created"
        const val KEY_PROVIDER_LINK_TYPE = "provider_link_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.link_your_id))

        if (intent.hasExtra(KEY_PROVIDER_LINK_TYPE)) {
            viewmodel.providerLinkType.set(
                ProviderLinkType.values()
                    [intent.getIntExtra(KEY_PROVIDER_LINK_TYPE, ProviderLinkType.LINK_NORMAL.ordinal)])
        }


        replaceFragment(ProviderSearchFragment.newInstance(),R.id.fragment_container)

        binding.baseToolbar.appToolbar.setNavigationOnClickListener{ onBackPressed() }
    }

    fun showPatientsAccounts() {
        addFragment(PatientAccountsFragment.newInstance(),R.id.fragment_container, true)
    }

    fun showVerifyOtpScreen() {
        binding.baseToolbar.appToolbar.visibility = View.GONE
        replaceFragment(VerifyOtpFragment.newInstance(),R.id.fragment_container, false)
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

     fun updateTitle(title: String) {
        binding.title = title
    }

}
