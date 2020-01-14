package `in`.org.projecteka.jataayu.provider.ui.fragment

import DividerItemDecorator
import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.featuresprovider.databinding.PatientAccountsFragmentBinding
import `in`.org.projecteka.jataayu.core.databinding.PatientAccountResultItemBinding
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.ui.handler.PatientAccountsScreenHandler
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.org.projecteka.jataayu.util.extension.setTitle
import `in`.org.projecteka.jataayu.util.extension.showLongToast
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PatientAccountsFragment : BaseFragment(), ItemClickCallback, PatientAccountsScreenHandler {
    private lateinit var binding: PatientAccountsFragmentBinding

    companion object {
        fun newInstance() = PatientAccountsFragment()

    }

    private val viewModel : ProviderSearchViewModel by sharedViewModel()

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
        binding.accountReferenceNumber = patient?.referenceNumber
        binding.canLinkAccounts = viewModel.canLinkAccounts()
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

    override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
        (itemViewBinding as PatientAccountResultItemBinding).cbCareContext.toggle()
        binding.canLinkAccounts = viewModel.canLinkAccounts()
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.link_accounts)
    }

    override fun onLinkAccountsClick(view: View) {
        observeLinkAccountsResponse()
        viewModel.linkPatientAccounts(viewModel.patientDiscoveryResponse.value!!)
    }

    private fun observeLinkAccountsResponse() {
        viewModel.linkAccountsResponse.observe(this, linkAccountsObserver)
    }

    private val linkAccountsObserver = Observer<LinkAccountsResponse> { _ ->
        showLongToast("Link accounts succeeded! \uD83D\uDD7A \uD83D\uDC83")
    }
}