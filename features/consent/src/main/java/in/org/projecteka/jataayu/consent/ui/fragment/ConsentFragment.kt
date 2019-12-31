package `in`.org.projecteka.jataayu.provider.ui.fragment

import DividerItemDecorator
import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.FragmentConsentsBinding
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.provider.viewmodel.ConsentViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConsentFragment : BaseFragment(), ItemClickCallback {

    companion object {
        fun newInstance() = ConsentFragment()
    }

    private val viewModel: ConsentViewModel by sharedViewModel()
    private lateinit var binding: FragmentConsentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsentsBinding.inflate(inflater)
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
        binding.accountReferenceNumber = patient?.referenceNumber
        binding.canLinkAccounts = viewModel.canLinkAccounts()

    }

    private fun renderPatientAccounts() {
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(
                this@ConsentFragment,
                viewModel.patientDiscoveryResponse.value?.patient?.careContexts!!
            )
            addItemDecoration(
                DividerItemDecorator(
                    ContextCompat.getDrawable(
                        context!!,
                        R.color.transparent
                    )!!
                )
            )
        }
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
//        (itemViewBinding as PatientAccountResultItemBinding).cbCareContext.toggle()
//        showLongToast((iDataBindingModel as CareContext).display)
    }
}