package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.callback.DeleteConsentCallback
import `in`.projecteka.jataayu.consent.databinding.ConsentRequestFragmentBinding
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.consent.ui.adapter.ConsentsListAdapter
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.ui.DateTimeUtils.Companion.isDateExpired
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.consent_request_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val INDEX_ACTIVE = 0
private const val INDEX_EXPIRED = 1
private const val INDEX_ALL = 2

abstract class ConsentsListFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    DeleteConsentCallback {
    abstract fun getConsentList(): List<Consent>
    abstract fun getNoNewConsentsMessage(): String
    abstract fun getConsentFlow(): ConsentFlow

    protected lateinit var binding: ConsentRequestFragmentBinding
    private lateinit var consentsListAdapter: ConsentsListAdapter

    private val viewModel: ConsentViewModel by sharedViewModel()

    companion object {
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
        viewModel.consentListResponse.observe(this, Observer {
            when (it) {
                is Loading -> showProgressBar(it.isLoading, getString(R.string.loading_requests))
                is Success -> {
                    binding.hideRequestsList = it.data?.requests.isNullOrEmpty()
                    viewModel.filterConsents(it.data?.requests)
                }
                is Failure -> {
                    context?.showAlertDialog(getString(R.string.failure), it.error.message ?: getString(R.string.something_went_wrong),
                        getString(android.R.string.ok))
                }
            }
        })
        viewModel.onClickConsentEvent.observe(this, Observer {
            val intent = Intent(context, ConsentDetailsActivity::class.java)
            intent.putExtra(CONSENT_FLOW, getConsentFlow().ordinal)
            startActivity(intent)

            if (getConsentFlow() == ConsentFlow.GRANTED_CONSENTS) {
                EventBus.getDefault().postSticky(it.id)
            } else {
                EventBus.getDefault().postSticky(it)
            }

            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        })
    }

    private fun initSpinner(selectedPosition: Int) {
        val arrayAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
            viewModel.populateFilterItems(resources,getConsentFlow()))
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
        consentsListAdapter = ConsentsListAdapter(
            viewModel,
            requests, this@ConsentsListFragment
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
            INDEX_ACTIVE -> filterRequests(getConsentList().filter { !isDateExpired(it.permission.dataExpiryAt) })
            INDEX_EXPIRED -> filterRequests(getConsentList().filter { isDateExpired(it.permission.dataExpiryAt) })
            INDEX_ALL -> filterRequests(getConsentList())
        }
    }


    private fun filterRequests(requests: List<Consent>) {
        (rvConsents.adapter as ConsentsListAdapter).updateData(requests)
    }

    @Subscribe
    fun onConsentGranted(messageEventType: MessageEventType) {
        if (messageEventType == MessageEventType.CONSENT_GRANTED) {
            viewModel.getConsents()
            EventBus.getDefault().unregister(this)
        }
    }

    override fun confirmRevoke(iDataBindingModel: IDataBindingModel) {
        val onClickListener = DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
            revokeConsent(iDataBindingModel)
        }
        context?.showAlertDialog(R.string.title_revoke_consent, R.string.msg_revoke_consent, R.string.btn_revoke_consent,
            onClickListener, android.R.string.cancel, null)
    }

    private fun revokeConsent(iDataBindingModel: IDataBindingModel) {
        consentsListAdapter.notifyItemRemoved(consentsListAdapter.removeItem(iDataBindingModel))
        val consents = ArrayList(viewModel.grantedConsentsList.value!!)
        consents.remove(iDataBindingModel)
        viewModel.grantedConsentsList.value = consents
        initSpinner(sp_request_filter.selectedItemPosition)
        viewModel.revokeConsent(iDataBindingModel as Consent)
    }
}