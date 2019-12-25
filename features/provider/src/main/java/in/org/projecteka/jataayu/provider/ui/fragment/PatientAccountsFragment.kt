package `in`.org.projecteka.jataayu.provider.ui.fragment

import DividerItemDecorator
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.featuresprovider.databinding.PatientAccountsFragmentBinding
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
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

    val viewModel : ProviderSearchViewModel by sharedViewModel()

    private lateinit var binding: PatientAccountsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PatientAccountsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        renderPatientAccounts()
    }

    private fun initBindings() {
        binding.selectedProviderName = viewModel.selectedProviderName
        val patient = viewModel.patientDiscoveryResponse.value?.patient
        binding.name = patient?.display
        binding.reference = patient?.referenceNumber
    }

    private fun renderPatientAccounts() {
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(
                this@PatientAccountsFragment,
                viewModel.patientDiscoveryResponse.value?.patient?.careContexts!!
            )
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
        }
    }

    override fun performItemClickAction(
        iDataBinding: IDataBinding,
        itemViewBinding: ViewDataBinding
    ) {
    }
}