package `in`.org.projecteka.jataayu.registration.ui.activity

import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.org.projecteka.jataayu.registration.R
import `in`.org.projecteka.jataayu.registration.ui.fragment.RegistrationFragment
import `in`.org.projecteka.jataayu.registration.ui.fragment.RegistrationOtpFragment
import `in`.org.projecteka.jataayu.util.extension.showLongToast
import android.os.Bundle

class RegistrationActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.register)
        addFragment(RegistrationFragment.newInstance())
    }

    fun redirectToOtpScreen(){
        addFragment(RegistrationOtpFragment.newInstance())
    }

    fun redirectToProviderSearchScreen() {
        showLongToast("Redirect now to provider search")
    }
}