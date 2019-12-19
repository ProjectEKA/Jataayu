package `in`.org.projecteka.jataayu.provider.ui.fragment

import DividerItemDecorator
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.featuresprovider.databinding.PatientAccountsFragmentBinding
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.provider.model.Patient
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PatientAccountsFragment : Fragment(), ItemClickCallback {

    companion object {
        fun newInstance() = PatientAccountsFragment()
    }

    private val viewModel : ProviderSearchViewModel by sharedViewModel()
    private val patientsList : List<Patient>? by lazy { viewModel.patients.value }

    private lateinit var binding : PatientAccountsFragmentBinding

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View {
        binding = PatientAccountsFragmentBinding.inflate(inflater)
        initBindings()
        renderSearchUi()
        return binding.root
    }

    private fun initBindings() {
        binding.selectedProviderName = viewModel.selectedProviderName
        binding.mobile = viewModel.mobile
        binding.accountsCountLabel = resources.getQuantityString(
            R.plurals.accounts_associated_found, patientsList?.size!!, patientsList?.size)
    }

    private fun renderSearchUi() {
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(this@PatientAccountsFragment, patientsList!!)
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
        }
    }

    override fun performItemClickAction(iDataBinding : IDataBinding,
                                        itemViewBinding : ViewDataBinding) {
    }
}