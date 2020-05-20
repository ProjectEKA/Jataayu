package `in`.projecteka.jataayu.provider.ui.fragment

import `in`.projecteka.featuresprovider.R
import `in`.projecteka.featuresprovider.databinding.PatientAccountsFragmentBinding
import `in`.projecteka.jataayu.core.databinding.PatientAccountResultItemBinding
import `in`.projecteka.jataayu.core.model.CareContext
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.projecteka.jataayu.provider.ui.ProviderActivity
import `in`.projecteka.jataayu.provider.ui.handler.PatientAccountsScreenHandler
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PatientAccountsFragment : BaseFragment(), ItemClickCallback, PatientAccountsScreenHandler,
    ResponseCallback {
    private lateinit var binding: PatientAccountsFragmentBinding

    companion object {
        fun newInstance() = PatientAccountsFragment()

    }

    private val viewModel : ProviderSearchViewModel by sharedViewModel()

    private lateinit var genericRecyclerViewAdapter: GenericRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PatientAccountsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderPatientAccounts()
        initBindings()
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.selectedProviderName = viewModel.selectedProviderName
        val patient = viewModel.patientDiscoveryResponse.value?.patient
        binding.name = patient?.display
        binding.accountReferenceNumber = patient?.referenceNumber
        binding.clickHandler = this
        val pair  = viewModel.canLinkAccounts(patient?.careContexts!!)
        binding.canLinkAccounts = pair.first
        updateLinkAccountButton(pair.second)

    }

    private fun renderPatientAccounts() {

        viewModel.makeAccountsSelected()
        val careContextsCount = viewModel.patientDiscoveryResponse.value?.patient?.careContexts?.count() ?: 0
        updateLinkAccountButton( careContextsCount)
        binding.hideNoLinkingAccountView = careContextsCount > 0
        binding.noLinkingAccounts = getString(R.string.link_accounts_alreday_done)


        genericRecyclerViewAdapter = GenericRecyclerViewAdapter(
            viewModel.patientDiscoveryResponse.value?.patient?.careContexts!!,
            this@PatientAccountsFragment
        )

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = genericRecyclerViewAdapter
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
        }
    }

    override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
        val checkbox = (itemViewBinding as PatientAccountResultItemBinding).cbCareContext
        checkbox.toggle()
        (iDataBindingModel as CareContext).contextChecked = checkbox.isChecked
        val pair = viewModel.canLinkAccounts(genericRecyclerViewAdapter.listOfBindingModels as List<CareContext>)
        binding.canLinkAccounts = pair.first
        updateLinkAccountButton(pair.second)

    }

    private fun updateLinkAccountButton(selectedItemsCount: Int) {
        if (selectedItemsCount > 0) {
            binding.linkSelected = getString(R.string.link_selected) + " (${selectedItemsCount})"
        } else {
            binding.linkSelected = getString(R.string.link_selected)
        }

    }

    override fun onVisible() {
        super.onVisible()
        (activity as? ProviderActivity)?.updateTitle(getString(R.string.link_accounts))
    }

    override fun onLinkAccountsClick(view: View) {
        viewModel.showProgress(true)
        observeLinkAccountsResponse()
        viewModel.linkPatientAccounts((genericRecyclerViewAdapter.listOfBindingModels as List<CareContext>), this)
    }

    private fun observeLinkAccountsResponse() {
        viewModel.linkAccountsResponse.observe(this, linkAccountsObserver)
    }

    private val linkAccountsObserver = Observer<LinkAccountsResponse> { _ ->
        (activity as ProviderActivity).showVerifyOtpScreen()
    }

    override fun <T> onSuccess(body: T?) {
        viewModel.showProgress(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        viewModel.showProgress(false)
        context?.showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        viewModel.showProgress(false)
        context?.showErrorDialog(t.localizedMessage)
    }
}