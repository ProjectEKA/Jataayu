package `in`.projecteka.jataayu.provider.ui.fragment

import `in`.projecteka.featuresprovider.R
import `in`.projecteka.featuresprovider.databinding.ProviderSearchFragmentBinding
import `in`.projecteka.jataayu.core.model.Hip
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.Request
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.provider.callback.TextWatcherCallback
import `in`.projecteka.jataayu.provider.domain.ProviderNameWatcher
import `in`.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.projecteka.jataayu.provider.ui.ProviderActivity
import `in`.projecteka.jataayu.provider.ui.adapter.ProviderSearchAdapter
import `in`.projecteka.jataayu.provider.ui.handler.ProviderSearchScreenHandler
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.sharedPref.DEFAULT_MOBILE
import `in`.projecteka.jataayu.util.sharedPref.MOBILE_NUMBER
import `in`.projecteka.jataayu.util.sharedPref.getString
import `in`.projecteka.jataayu.util.ui.UiUtils
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProviderSearchFragment : BaseFragment(), ItemClickCallback, TextWatcherCallback,
    ProviderSearchScreenHandler, ResponseCallback {
    private lateinit var binding: ProviderSearchFragmentBinding

    companion object {
        fun newInstance() = ProviderSearchFragment()

    }
    private val viewModel: ProviderSearchViewModel by sharedViewModel()
    private lateinit var lastQuery: String
    private lateinit var selectedProvider : ProviderInfo
    private lateinit var providersList: ProviderSearchAdapter

    private val providersObserver = Observer<List<ProviderInfo>> { providerNames ->
        providersList.updateData(lastQuery, providerNames)
        showNoResultsFoundView(providerNames.isEmpty())
    }

    private val patientAccountsObserver =
        Observer<PatientDiscoveryResponse?> { _ ->
            showPatientAccountsList()
        }

    private fun showPatientAccountsList() {
        (activity as ProviderActivity).showPatientsAccounts()
    }

    private fun observeProviders() {
        viewModel.providers.observe(this, providersObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProviderSearchFragmentBinding.inflate(inflater)
        initBindings()
        renderSearchUi()
        return binding.root
    }

    private fun initBindings() {
        binding.clearButtonVisibility = GONE
        binding.inEditMode = true
        binding.selectedProviderName = viewModel.selectedProviderName
        binding.clickHandler = this
        binding.textWatcher = ProviderNameWatcher(this, 1)
        binding.mobile = context?.getString(MOBILE_NUMBER, DEFAULT_MOBILE)
    }

    private fun renderSearchUi() {
        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)
        providersList = ProviderSearchAdapter(this)
        binding.rvSearchResults.adapter = providersList
        binding.rvSearchResults.addOnScrollListener(onScrollListener)
    }

    private fun showNoResultsFoundView(show: Boolean) {
        binding.noResultsFoundView.visibility = if (show) VISIBLE else GONE
    }

    override fun onVisible() {
        if (!binding.inEditMode!!)
            setTitle(R.string.confirm_provider)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProviders()
        setTitle(R.string.link_provider)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        providersList.updateData(lastQuery, emptyList())
        binding.inEditMode = false
        UiUtils.hideKeyboard(activity as Activity)
        selectedProvider = iDataBindingModel as ProviderInfo
        binding.tvSelectedProvider.text = selectedProvider.nameCityPair()
        viewModel.selectedProviderName = selectedProvider.nameCityPair()
        binding.tvSearchProviderLabel.text = getString(R.string.we_will_be_sending_info_to)
        binding.svProvider.clearFocus()
        binding.tvSelectedProvider.postDelayed({ binding.tvSelectedProvider.requestFocus() }, 100)
        setTitle(R.string.confirm_provider)
        binding.btnSearch.text = getString(R.string.confirm_provider)
    }

    private val onScrollListener =
        object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                UiUtils.hideKeyboard(context as Activity)
            }
        }

    override fun onTextChanged(changedText: CharSequence?, clearButtonVisibility: Int) {
        viewModel.getProviders(changedText.toString())
        lastQuery = changedText.toString()
        binding.clearButtonVisibility = VISIBLE
    }

    override fun onTextCleared(clearButtonVisibility: Int) {
        viewModel.clearList()
        providersList.updateData(listOf())
        binding.clearButtonVisibility = GONE
        showNoResultsFoundView(false)
    }

    override fun onClearTextButtonClick(view: View) {
        binding.svProvider.text.clear()
        binding.inEditMode = true
    }

    override fun onClearSelectionClick(view: View) {
        binding.svProvider.setText(lastQuery)
        binding.svProvider.setSelection(lastQuery.length)
        binding.inEditMode = true
        binding.svProvider.requestFocus()
        setTitle(R.string.link_provider)
        binding.btnSearch.text = getString(R.string.link_provider)
    }

    override fun onSearchButtonClick(view: View) {
        viewModel.getPatientAccounts(Request(Hip(selectedProvider.hip.id, selectedProvider.hip.name)), this)
        showProgressBar(true, getString(R.string.looking_up_info))
        observePatients()
    }

    private fun observePatients() =
        viewModel.patientDiscoveryResponse.observe(this, patientAccountsObserver)

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        showProgressBar(false)
        context?.showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        context?.showErrorDialog(t.localizedMessage)
    }
}