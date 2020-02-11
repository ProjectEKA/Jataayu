package `in`.org.projecteka.jataayu.registration.ui.activity

import `in`.org.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.org.projecteka.jataayu.registration.ui.fragment.RegistrationFragment
import `in`.org.projecteka.jataayu.registration.ui.fragment.RegistrationOtpFragment
import android.os.Bundle

class RegistrationActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFragment(RegistrationFragment.newInstance())
    }

    fun redirectToOtpScreen(){
        addFragment(RegistrationOtpFragment.newInstance())
    }
}