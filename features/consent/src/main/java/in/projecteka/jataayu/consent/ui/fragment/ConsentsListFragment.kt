package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.ConsentRequestFragmentBinding
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.ui.DateTimeUtils.Companion.isDateExpired
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
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val INDEX_ACTIVE = 0
private const val INDEX_EXPIRED = 1
private const val INDEX_ALL = 2

abstract class ConsentsListFragment : BaseFragment(), ItemClickCallback,
    AdapterView.OnItemSelectedListener {
    abstract fun getConsentList(): List<Consent>
    abstract fun getNoNewConsentsMessage(): String
    abstract fun getConsentFlow(): ConsentFlow

    protected lateinit var binding: ConsentRequestFragmentBinding
    private lateinit var flow: ConsentFlow
    private val viewModel: ConsentViewModel by sharedViewModel()

    companion object {
        const val CONSENT_FLOW = "consent_flow"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentRequestFragmentBinding.inflate(inflater)
        flow = getConsentFlow()
        initBindings()
        return binding.root
    }


    private fun initObservers() {
        viewModel.consentListResponse.observe(this, Observer {
            when (it) {
                is Loading -> showProgressBar(it.isLoading, getString(R.string.loading_requests))
                is Success -> {
                    binding.hideRequestsList = !it.data?.requests.isNullOrEmpty()
                    viewModel.filterConsents(it.data?.requests)
                }
                is Failure -> {
                    //do nothing for now.
                }
            }
        })
    }

    private fun initSpinner(selectedPosition: Int) {
        val arrayAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
            viewModel.populateFilterItems(resources, getConsentFlow())
        )
        binding.spRequestFilter.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spRequestFilter.setSelection(selectedPosition)
    }

    private fun initBindings() {
        binding.noNewConsentsMessage = getNoNewConsentsMessage()
        binding.listener = this
        binding.hideRequestsList = true
        showProgressBar(false)
        initSpinner(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getConsents()
        initObservers()
    }


    protected fun renderConsentRequests(requests: List<Consent>, selectedSpinnerPosition: Int) {
        with(rvConsents) {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(
                this@ConsentsListFragment,
                requests
            )
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
        }
        initSpinner(selectedSpinnerPosition)
        sp_request_filter.setSelection(INDEX_ACTIVE)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        val intent = Intent(context, ConsentDetailsActivity::class.java)
        intent.putExtra(CONSENT_FLOW, flow?.ordinal)
        startActivity(intent)
        EventBus.getDefault().postSticky(iDataBindingModel as Consent)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            INDEX_ACTIVE -> filterRequests(getConsentList().filter { !isDateExpired(it.permission.dataExpiryAt) })
            INDEX_EXPIRED -> filterRequests(getConsentList().filter { isDateExpired(it.permission.dataExpiryAt) })
            INDEX_ALL -> filterRequests(getConsentList())
        }
    }


    private fun filterRequests(requests: List<Consent>) {
        (rvConsents.adapter as GenericRecyclerViewAdapter).updateData(requests)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onConsentGranted(messageEventType: MessageEventType) {
        viewModel.getConsents()
        EventBus.getDefault().unregister(this)
    }
}
