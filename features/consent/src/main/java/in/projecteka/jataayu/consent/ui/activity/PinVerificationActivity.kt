package `in`.projecteka.jataayu.consent.ui.activity

import `in`.projecteka.jataayu.consent.ui.fragment.UserVerificationFragment
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseDialogFragment
import android.os.Bundle

class PinVerificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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