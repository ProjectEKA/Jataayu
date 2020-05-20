package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.ui.fragment.CreatePinFragment
import `in`.projecteka.jataayu.consent.viewmodel.CreatePinActivityViewModel
import `in`.projecteka.jataayu.consent.viewmodel.PinVerificationViewModel
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePinActivity : BaseActivity<BaseActivityBinding>() {
    override fun layoutId(): Int = R.layout.activity_create_pin

    private val viewModel: CreatePinActivityViewModel by viewModel()
//    private val viewModel: UserVerificationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(PinVerificationViewModel.KEY_SCOPE_TYPE)) {
            viewModel.scopeType.set(
                ConsentScopeType.values()[intent.getIntExtra(
                    PinVerificationViewModel.KEY_SCOPE_TYPE, ConsentScopeType.SCOPE_GRAND.ordinal)])
            viewModel.scopeType.get()
        }

        addFragment(CreatePinFragment.newInstance(), R.id.fragment_container)

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