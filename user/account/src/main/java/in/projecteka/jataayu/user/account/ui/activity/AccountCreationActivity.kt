package `in`.projecteka.jataayu.account

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import android.os.Bundle


class AccountCreationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(CreateAccountFragment.newInstance())
    }

}
