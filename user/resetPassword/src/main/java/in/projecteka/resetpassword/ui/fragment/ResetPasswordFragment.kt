package `in`.projecteka.resetpassword.ui.fragment

import `in`.projecteka.forgotpassword.R
import `in`.projecteka.forgotpassword.databinding.FragmentResetPasswordBinding
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordActivityViewModel
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordFragmentViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel: ResetPasswordFragmentViewModel by viewModel()
    private val parentViewModel: ResetPasswordActivityViewModel by sharedViewModel()

    companion object {
        fun newInstance() = ResetPasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initObservers()
        parentViewModel.tempToken?.let {
            viewModel.init(it)
        }
    }

    private fun initObservers() {
//        viewModel.initializeAllTheValues()
        binding.etCreatePassword.addTextChangedListener { text ->
            viewModel.validatePassword()
        }
        binding.etConfirmPassword.addTextChangedListener { text ->
            viewModel.validatePassword()
        }

        viewModel.resetPasswordResponse.observe(this, Observer {
            when(it){
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
                    showLongToast(getString(R.string.password_changed))
                    activity?.finish()
                }
                is PartialFailure -> {
                    activity?.showAlertDialog(
                        getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok)
                    )
                }
                is Failure -> {
                    activity?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.parentViewModel = parentViewModel

    }

}
