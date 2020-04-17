package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.presentation.databinding.BaseActivityBinding
import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import android.os.Bundle


class AccountCreationActivity : BaseActivity<BaseActivityBinding>() {

    override fun layoutId(): Int = R.layout.base_activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(CreateAccountFragment.newInstance(),R.id.fragment_container)
    }

}
