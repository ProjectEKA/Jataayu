package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.ConsentRequestFragmentBinding
import `in`.org.projecteka.jataayu.consent.model.ConsentFlow
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.network.utils.ResponseCallback
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.util.ui.DateTimeUtils
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
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RequestListFragment : BaseFragment(), ItemClickCallback, AdapterView.OnItemSelectedListener,
    ResponseCallback {

    private lateinit var binding: ConsentRequestFragmentBinding
    private val INDEX_REQUESTED_CONSENTS = 1
    private val INDEX_EXPIRED_CONSENT_REQUESTS = 2
    private var flow: ConsentFlow? = ConsentFlow.REQUESTED_CONSENTS
    private val viewModel: ConsentViewModel by sharedViewModel()

    companion object {
        const val CONSENT_FLOW = "consent_flow"
        fun newInstance(type: Int): RequestListFragment {
            val fragment = RequestListFragment()
            val bundle = Bundle()
            bundle.putInt(CONSENT_FLOW, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val consentObserver = Observer<ConsentsListResponse?> {
        renderConsentRequests(it?.requests!!, binding.spRequestFilter.selectedItemPosition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentRequestFragmentBinding.inflate(inflater)
        this.arguments?.let {
            val consentFlowValue = it.getInt(CONSENT_FLOW, ConsentFlow.REQUESTED_CONSENTS.ordinal)
            flow = ConsentFlow.values()[consentFlowValue]
        }
        initBindings()
        return binding.root
    }

    private fun initSpinner(selectedPosition: Int) {
        val arrayAdapter =
            ArrayAdapter<String>(
                context!!, android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                viewModel.populateFilterItems(resources)
            )
        binding.spRequestFilter.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spRequestFilter.setSelection(selectedPosition)
    }

    private fun initBindings() {
        binding.requestCount = getString(R.string.all_requests, 0)
        if (flow == ConsentFlow.GRANTED_CONSENTS){
            binding.noNewConsentsMessage = getString(R.string.no_granted_consents)
        } else{
            binding.noNewConsentsMessage = getString(R.string.no_new_consent_requests)
        }

        binding.listener = this
        binding.hideRequestsList = true
        showProgressBar(false)
        initSpinner(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.consentsListResponse.observe(this, consentObserver)
        viewModel.getConsents(this)
        showProgressBar(true, getString(R.string.loading_requests))
    }

    private fun renderConsentRequests(requests: List<Consent>, selectedSpinnerPosition: Int) {
        showProgressBar(false)
        binding.hideRequestsList = !viewModel.isRequestAvailable()
        rvConsents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(
                this@RequestListFragment,
                requests
            )
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
        }
        initSpinner(selectedSpinnerPosition)
        sp_request_filter.setSelection(INDEX_REQUESTED_CONSENTS)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        val intent = Intent(context, ConsentDetailsActivity::class.java)
        intent.putExtra(CONSENT_FLOW, flow?.ordinal)
        startActivity(intent)
        EventBus.getDefault().postSticky(iDataBindingModel as Consent)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            INDEX_REQUESTED_CONSENTS -> filterRequests(viewModel.requests.filter {
                !DateTimeUtils.isDateExpired(it.permission.dataExpiryAt)
            })
            INDEX_EXPIRED_CONSENT_REQUESTS -> filterRequests(viewModel.requests.filter {
                DateTimeUtils.isDateExpired(it.permission.dataExpiryAt)
            })
            else -> filterRequests(viewModel.requests)
        }
    }

    private fun filterRequests(requests: List<Consent>) {
        (rvConsents.adapter as GenericRecyclerViewAdapter).updateData(requests)
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
        (body as? ConsentsListResponse)?.requests?.let { viewModel.requests = it }
    }

    override fun onFailure(errorBody: ResponseBody) {
        showProgressBar(false)
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
    }
}