package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.registration.ui.fragment.LoginFragment
import android.os.Bundle

class LoginActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(LoginFragment.newInstance())
    }

    override fun onResume() {
        super.onResume()
    }
}