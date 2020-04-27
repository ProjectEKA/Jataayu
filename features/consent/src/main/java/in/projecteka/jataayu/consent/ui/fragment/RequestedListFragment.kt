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
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.ui.DateTimeUtils.Companion.isDateExpired
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
import kotlinx.android.synthetic.main.consent_request_fragment.*
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val INDEX_ACTIVE = 0
private const val INDEX_EXPIRED = 1
private const val INDEX_DENIED = 2
private const val INDEX_ALL = 3

class RequestedListFragment : BaseFragment(), AdapterView.OnItemSelectedListener, ItemClickCallback {

    protected lateinit var binding: ConsentRequestFragmentBinding
    private lateinit var consentsListAdapter: ConsentsListAdapter

    private val viewModel: RequestedListViewModel by sharedViewModel()
    private val parentViewModel: ConsentHostFragmentViewModel by sharedViewModel()

    companion object {
        fun newInstance() = RequestedListFragment()
        const val CONSENT_FLOW = "consent_flow"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentRequestFragmentBinding.inflate(inflater)
        initBindings()
        return binding.root
    }

    private fun initObservers() {
        viewModel.requestedConsentsList.observe(this, Observer<List<Consent>?> { it ->
            it?.let { consentList ->
                val hiuList = consentList.map { consent -> consent.hiu }
                getNamesOf(hiuList)
            }
        })

        viewModel.consentListResponse.observe(this, Observer {
            when (it) {
                is Loading -> {
                    showProgressBar(it.isLoading, getString(R.string.loading_requests))
                }
                is Success -> {
                    parentViewModel.showRefreshing(false)
                    viewModel.filterConsents(it.data?.requests)
                    binding.hideRequestsList = viewModel.requestedConsentsList.value.isNullOrEmpty()
                    binding.hideFilter = binding.hideRequestsList
                }
                is PartialFailure -> {
                    context?.showAlertDialog(getString(R.string.failure), it.error?.message,
                        getString(android.R.string.ok))
                }
            }
        })
        parentViewModel.pullToRefreshEvent.observe(viewLifecycleOwner, Observer{
            if (it) {
                viewModel.getConsents()
            }
        })
    }

    private fun initSpinner(selectedPosition: Int) {
        val arrayAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
            viewModel.populateFilterItems(resources,

                getConsentFlow())
        )
        binding.spRequestFilter.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spRequestFilter.setSelection(selectedPosition)
    }

    private fun initBindings() {
        binding.noNewConsentsMessage = getString(R.string.no_new_consent_requests)
        binding.listener = this
        binding.hideRequestsList = true
        binding.hideFilter =  binding.hideRequestsList
        showProgressBar(false)
        initSpinner(0)
    }

    private fun getNamesOf(hiuList: List<HipHiuIdentifiable>) {
        val hipHiuNameResponse = viewModel.fetchHipHiuNamesOf(hiuList)
        hipHiuNameResponse.observe(this, Observer {
            viewModel.requestedConsentsList.value?.let { consentList ->
                if(it.status) {
                    consentList.forEach { consent -> consent.hiu.name = it.nameMap[consent.hiu.getId()] ?: "" }
                    renderConsentRequests(consentList, binding.spRequestFilter.selectedItemPosition)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.getConsents()
    }

    protected fun renderConsentRequests(requests: List<Consent>, selectedSpinnerPosition: Int) {
        consentsListAdapter = ConsentsListAdapter(
            this@RequestedListFragment,
            requests
        )
        rvConsents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = consentsListAdapter
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
        }
        initSpinner(selectedSpinnerPosition)
        sp_request_filter.setSelection(INDEX_ACTIVE)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            INDEX_ACTIVE -> filterRequests(getConsentList().filter { !isDateExpired(it.permission.dataEraseAt) && it.status != RequestStatus.DENIED })
            INDEX_EXPIRED -> filterRequests(getConsentList().filter { isDateExpired(it.permission.dataEraseAt) && it.status != RequestStatus.DENIED})
            INDEX_DENIED -> filterRequests(getConsentList().filter { it.status.equals(RequestStatus.DENIED) })
            INDEX_ALL -> filterRequests(getConsentList())
        }
    }

    private fun filterRequests(requests: List<Consent>) {
        (rvConsents.adapter as ConsentsListAdapter).updateData(requests)
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

    fun getConsentList(): List<Consent> {
        return viewModel.requestedConsentsList.value!!
    }

    fun getConsentFlow(): ConsentFlow {
        return ConsentFlow.REQUESTED_CONSENTS
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
    }
}