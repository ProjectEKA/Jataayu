package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.databinding.PatientAccountsFragmentBinding
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject

class PatientAccountsFragment : Fragment(), ItemClickCallback {

    companion object {
        fun newInstance() = PatientAccountsFragment()
    }

    private val viewModel : ProviderSearchViewModel by inject()
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
    }

    private fun renderSearchUi() {
        binding.rvSearchResults.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.rvSearchResults.adapter = GenericRecyclerViewAdapter(this, viewModel.patients.value!!)
    }

    override fun performItemClickAction(iDataBinding : IDataBinding,
                                        itemViewBinding : ViewDataBinding) {

    }
}