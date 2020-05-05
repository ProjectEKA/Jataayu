package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.ui.fragment.UserVerificationFragment
import `in`.projecteka.jataayu.consent.viewmodel.PinVerificationViewModel
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinVerificationActivity : BaseActivity<BaseActivityBinding>() {

    override fun layoutId(): Int = R.layout.base_activity

    private val viewModel: PinVerificationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(PinVerificationViewModel.KEY_SCOPE_TYPE)) {
            viewModel.scopeType.set(ConsentScopeType.values()[intent.getIntExtra(PinVerificationViewModel.KEY_SCOPE_TYPE, ConsentScopeType.SCOPE_GRAND.ordinal)])
        }

        UserVerificationFragment.newInstance()
            .show(supportFragmentManager, UserVerificationFragment::class.java.name)

        //TODO : check if it is needed here
        supportFragmentManager.apply {
            addOnBackStackChangedListener {
                if (fragments.isNotEmpty()) fragments.last {
                    (it as? BaseDialogFragment)?.onVisible()
                    return@last true
                }
            }
        }
    }
}