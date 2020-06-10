package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.SuccessPageFragmentBinding
import `in`.projecteka.jataayu.user.account.viewmodel.AccountCreationActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.SuccessPageViewModel
import `in`.projecteka.jataayu.util.startDashboard
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuccessPageFragment : Fragment() {

    private lateinit var binding: SuccessPageFragmentBinding

    companion object {
        fun newInstance() = SuccessPageFragment()
        const val KEY_ACCOUNT_CREATED = "account_created"
    }

    private val viewModel: SuccessPageViewModel by viewModel()
    private val parentVM: AccountCreationActivityViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SuccessPageFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        parentVM.appBarTitle.set(getString(R.string.confirmation))

        viewModel.fullNameLbl.set(parentVM.fullName)
        viewModel.cmIdInfoLbl.set(SpannableStringBuilder()
            .bold { append("${parentVM.cmId} ") }
            .append(getString(R.string.cm_id_info)))
    }

    private fun initBindings(){
        binding.viewModel = viewModel
        binding.btnConfirmRegistration.setOnClickListener {
            activity?.let {
                it.finish()
                startDashboard(it) {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }
        }
    }

}
