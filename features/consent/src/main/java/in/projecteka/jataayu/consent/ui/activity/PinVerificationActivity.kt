package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.ui.fragment.UserVerificationFragment
import `in`.projecteka.jataayu.consent.viewmodel.PinVerificationViewModel
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.activity.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinVerificationActivity : BaseActivity<BaseActivityBinding>() {

    override fun layoutId(): Int = R.layout.activity_pin_verification

    private val viewModel: PinVerificationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(PinVerificationViewModel.KEY_SCOPE_TYPE)) {
            viewModel.scopeType.set(ConsentScopeType.values()[intent.getIntExtra(PinVerificationViewModel.KEY_SCOPE_TYPE, ConsentScopeType.SCOPE_GRAND.ordinal)])
        }

        addFragment(UserVerificationFragment.newInstance(), R.id.fragment_container)

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