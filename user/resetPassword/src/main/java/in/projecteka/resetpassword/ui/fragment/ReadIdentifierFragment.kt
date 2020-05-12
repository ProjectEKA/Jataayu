package `in`.projecteka.resetpassword.ui.fragment

import `in`.projecteka.forgotpassword.R
import `in`.projecteka.forgotpassword.databinding.FragmentConsentManagerIdBinding
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.resetpassword.viewmodel.ReadIdentifierFragmentViewModel
import `in`.projecteka.resetpassword.viewmodel.ResetPasswordActivityViewModel
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadIdentifierFragment : Fragment() {

    private lateinit var binding: FragmentConsentManagerIdBinding
    private val viewModel: ReadIdentifierFragmentViewModel by viewModel()
    private val parentViewModel: ResetPasswordActivityViewModel by sharedViewModel()

    companion object {
        fun newInstance() = ReadIdentifierFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsentManagerIdBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initObservers()
    }

    private fun initObservers() {
        viewModel.generateOtpResponse.observe(this, Observer {
            when(it){
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
//                    parentViewModel.addedConsentManagerId.set(it.data?.otpMediumValue)
                    parentViewModel.sessionId = it.data?.sessionId
                    parentViewModel.onOtpFragmentRedirectRequest()
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
        viewModel.consentManagerIdField.observe(this, Observer {
            parentViewModel.consentManagerId = it
        })
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.etConsentManagerId.addTextChangedListener { text: Editable? ->
            viewModel.validateConsentManagerId(viewModel.inputConsentManagerId.get().toString())
        }
    }

}
