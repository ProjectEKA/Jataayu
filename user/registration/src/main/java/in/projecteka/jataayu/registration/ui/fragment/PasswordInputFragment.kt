package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.PasswordInputFragmentBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.registration.viewmodel.PasswordInputViewModel
import `in`.projecteka.jataayu.util.startForgotPassword
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordInputFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordInputFragment()
        const val KEY_CONSENT_ID = "consent_id"
    }

    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val viewModel: PasswordInputViewModel by viewModel()

    private lateinit var binding: PasswordInputFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PasswordInputFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initObserver()
    }

    private fun initObserver() {
        binding.btnLogin.setOnClickListener {
            viewModel.onLoginClicked(loginViewModel.cmId)
        }

        viewModel.loginByPasswordResponse.observe(viewLifecycleOwner, Observer {
          when(it) {
            is Loading -> viewModel.showProgress(it.isLoading, R.string.logging_in)
              is Success ->  {
                  viewModel.onLoginSuccess(it.data!!)
                  loginViewModel.loginResponseSuccessEvent.call()
              }
              is Failure -> {
                  context?.showErrorDialog(getString(R.string.something_went_wrong))
              }
              is PartialFailure -> {
                  viewModel.onLoginFailure(it.error, resources)
              }
          }
        })

        viewModel.errorDialogEvent.observe(viewLifecycleOwner, Observer {
            context?.showErrorDialog(it)
        })

        viewModel.onClickForgotPasswordEvent.observe(viewLifecycleOwner, Observer {
            startForgotPassword(activity!!) {
                putExtra(KEY_CONSENT_ID, loginViewModel.cmId)
            }
        })
    }

}
