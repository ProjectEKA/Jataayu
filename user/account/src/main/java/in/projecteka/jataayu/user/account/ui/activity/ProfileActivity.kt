package `in`.projecteka.jataayu.user.account.ui.activity

import `in`.projecteka.jataayu.presentation.ui.BaseActivity
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ActivityProfileBinding
import `in`.projecteka.jataayu.user.account.ui.fragment.ProfileFragment
import `in`.projecteka.jataayu.user.account.viewmodel.ProfileActivityViewModel
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    private val viewmodel: ProfileActivityViewModel by viewModel()

    companion object {
       const val snackbarMargin = 16
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        viewmodel.init()
    }

    private fun initObservers() {
        viewmodel.redirectTo.observe(this, Observer {
            addFragment(when(it){
                ProfileActivityViewModel.Show.VIEW_PROFILE ->
                    ProfileFragment.newInstance()
            }, R.id.fragment_container)
        })
    }

    override fun layoutId(): Int =
        R.layout.activity_profile
}
