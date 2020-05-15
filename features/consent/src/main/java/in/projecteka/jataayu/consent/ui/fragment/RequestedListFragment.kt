package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.ConsentRequestFragmentBinding
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.consent.ui.adapter.ConsentsListAdapter
import `in`.projecteka.jataayu.consent.viewmodel.ConsentHostFragmentViewModel
import `in`.projecteka.jataayu.consent.viewmodel.ConsentHostFragmentViewModel.Companion.REQUEST_CONSENT_DETAILS
import `in`.projecteka.jataayu.consent.viewmodel.RequestedListViewModel
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RequestedListFragment : BaseFragment(), AdapterView.OnItemSelectedListener, ItemClickCallback {

    private lateinit var binding: ConsentRequestFragmentBinding
    private lateinit var consentsListAdapter: ConsentsListAdapter

    private val viewModel: RequestedListViewModel by sharedViewModel()
    private val parentViewModel: ConsentHostFragmentViewModel by sharedViewModel()

    companion object {
        fun newInstance() = RequestedListFragment()
        const val CONSENT_FLOW = "consent_flow"
    }

    //region life cycle methods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentRequestFragmentBinding.inflate(inflater)
        initBindings()
        initRecyclerViewAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.getConsents(offset = 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CONSENT_DETAILS && resultCode == Activity.RESULT_OK) {
            data?.let {
                val eventType = data.getStringExtra(ConsentHostFragmentViewModel.KEY_CONSENT_EVENT_TYPE)
                if (eventType == ConsentHostFragmentViewModel.KEY_EVENT_GRANT) {
                    parentViewModel.showToastEvent.value = getString(R.string.consent_granted)
                } else if (eventType == ConsentHostFragmentViewModel.KEY_EVENT_DENY) {
                    parentViewModel.showToastEvent.value =getString(R.string.consent_denied)
                }
            }
            parentViewModel.pullToRefreshEvent.value = true
        }
        if (requestCode == REQUEST_CONSENT_DETAILS && resultCode == Activity.RESULT_CANCELED) {
            parentViewModel.pullToRefreshEvent.value = true
        }
    }

    //endregion

    //region observers

    private fun initObservers() {

        viewModel.consentListResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Loading -> {
                    if (viewModel.isLoadingMore.get() != View.VISIBLE) {
                        viewModel.showProgress(response.isLoading, R.string.loading_requests)
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
                is Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    viewModel.updateRequestedConsentList(response.data!!)
                    response.data?.requests?.let {
                        val hiuList = it.map { consent -> consent.hiu }
                        getNamesOf(hiuList)
                    }
                    parentViewModel.showRefreshing(false)

                    binding.hideRequestsList = response.data?.requests.isNullOrEmpty()
                    binding.hideFilter = false
                }
                is PartialFailure -> {
                    context?.showAlertDialog(getString(R.string.failure), response.error?.message,
                        getString(android.R.string.ok))
                }
            }
        })

        viewModel.currentStatus.observe(viewLifecycleOwner, Observer {
            clearRecylerView()
        })


        parentViewModel.pullToRefreshEvent.observe(viewLifecycleOwner, Observer{
            if (it) {
                clearRecylerView()
            }
        })
    }

    //endregion

    //region view setup

    private fun initSpinner(selectedPosition: Int) {
        val arrayAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
            viewModel.populateFilterItems(resources)
        )
        binding.spRequestFilter.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spRequestFilter.setSelection(selectedPosition)
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.noNewConsentsMessage = getString(R.string.no_new_consent_requests)
        binding.listener = this
        binding.hideRequestsList = true
        binding.hideFilter =  binding.hideRequestsList
        viewModel.showProgress(false)
        initSpinner(0)
    }

    private fun initRecyclerViewAdapter() {
        consentsListAdapter = ConsentsListAdapter(
            this@RequestedListFragment, listOf()
        )
        binding.rvConsents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = consentsListAdapter
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
            setupScrollListener()
        }
    }

    //endregion

    //region call back methods

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.updateFilterSelectedItem(position)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        if (iDataBindingModel is Consent) {
            val intent = Intent(context, ConsentDetailsActivity::class.java)
            intent.putExtra(CONSENT_FLOW, getConsentFlow().ordinal)
            this.startActivityForResult(intent, REQUEST_CONSENT_DETAILS)
            EventBus.getDefault().postSticky(iDataBindingModel)
        }
    }

    //endregion

    //region private methods

    private fun getNamesOf(hiuList: List<HipHiuIdentifiable>) {
        val hipHiuNameResponse = viewModel.fetchHipHiuNamesOf(hiuList)
        hipHiuNameResponse.observe(this, Observer {
            viewModel.requestedConsentsList.value?.requests?.let { consentList ->
                if(it.status) {
                    consentList.forEach { consent -> consent.hiu.name = it.nameMap[consent.hiu.getId()] ?: "" }
                    renderConsentRequests(consentList, binding.spRequestFilter.selectedItemPosition)
                }
            }
        })
    }

    private fun renderConsentRequests(requests: List<Consent>, selectedSpinnerPosition: Int) {
        consentsListAdapter.updateData(requests)
    }

    fun getConsentFlow(): ConsentFlow {
        return ConsentFlow.REQUESTED_CONSENTS
    }

    private fun clearRecylerView() {
        consentsListAdapter.clearAll()
        viewModel.getConsents(offset = 0)
    }

    private fun setupScrollListener() {
        binding.rvConsents.addOnScrollListener(viewModel.paginationScrollListener)
    }

    //endregion
}