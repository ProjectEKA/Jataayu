package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.ui.fragment.CreatePinFragment
import `in`.projecteka.jataayu.consent.viewmodel.UserVerificationViewModel
import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePinActivity : BaseActivity<BaseActivityBinding>() {
    override fun layoutId(): Int = R.layout.base_activity

    private val viewModel: UserVerificationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CreatePinFragment.newInstance().show(supportFragmentManager, CreatePinFragment::class.java.name)

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