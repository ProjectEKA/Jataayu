package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.featuresprovider.databinding.PatientAccountsFragmentBinding
import `in`.org.projecteka.jataayu.core.databinding.PatientAccountResultItemBinding
import `in`.org.projecteka.jataayu.core.model.CareContext
import `in`.org.projecteka.jataayu.network.utils.ResponseCallback
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.provider.model.LinkAccountsResponse
import `in`.org.projecteka.jataayu.provider.ui.ProviderActivity
import `in`.org.projecteka.jataayu.provider.ui.handler.PatientAccountsScreenHandler
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.org.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.ResponseBody
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
        binding.selectedProviderName = viewModel.selectedProviderName
        val patient = viewModel.patientDiscoveryResponse.value?.patient
        binding.name = patient?.display
        binding.accountReferenceNumber = patient?.referenceNumber
        binding.clickHandler = this
        binding.canLinkAccounts = viewModel.canLinkAccounts(patient?.careContexts!!)
    }

    private fun renderPatientAccounts() {

        viewModel.makeAccountsSelected()

        genericRecyclerViewAdapter = GenericRecyclerViewAdapter(
            this@PatientAccountsFragment,
            viewModel.patientDiscoveryResponse.value?.patient?.careContexts!!)

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
        binding.canLinkAccounts = viewModel.canLinkAccounts(genericRecyclerViewAdapter.listOfBindingModels as List<CareContext>)
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.link_accounts)
    }

    override fun onLinkAccountsClick(view: View) {
        showProgressBar(true)
        observeLinkAccountsResponse()
        viewModel.linkPatientAccounts(viewModel.patientDiscoveryResponse.value!!, this)
    }

    private fun observeLinkAccountsResponse() {
        viewModel.linkAccountsResponse.observe(this, linkAccountsObserver)
    }

    private val linkAccountsObserver = Observer<LinkAccountsResponse> { _ ->
        (activity as ProviderActivity).showVerifyOtpScreen()
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ResponseBody) {
        showProgressBar(false)
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
    }
}