package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.callback.DeleteConsentCallback
import `in`.projecteka.jataayu.consent.databinding.ConsentRequestFragmentBinding
import `in`.projecteka.jataayu.consent.model.ConsentFlow
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.consent.ui.activity.PinVerificationActivity
import `in`.projecteka.jataayu.consent.ui.adapter.ConsentsListAdapter
import `in`.projecteka.jataayu.consent.viewmodel.ConsentHostFragmentViewModel
import `in`.projecteka.jataayu.consent.viewmodel.GrantedConsentListViewModel
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.core.model.RequestStatus
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.extension.showShortToast
import `in`.projecteka.jataayu.util.sharedPref.getConsentTempToken
import `in`.projecteka.jataayu.util.ui.DateTimeUtils.Companion.isDateExpired
import android.R.layout
import android.R.string
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
private const val INDEX_ALL = 2

class ConsentListFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    DeleteConsentCallback, ItemClickCallback {
    private lateinit var consentToRevoke: Consent

    private lateinit var binding: ConsentRequestFragmentBinding
    private lateinit var consentsListAdapter: ConsentsListAdapter

    private val viewModel: GrantedConsentListViewModel by sharedViewModel()
    private val parentViewModel: ConsentHostFragmentViewModel by sharedViewModel()

    companion object {
        fun newInstance() = ConsentListFragment()
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
        
        viewModel.grantedConsentsList.observe(this, Observer<List<Consent>?> {
            it?.let {
                val hiuList = it.map { consent -> consent.hiu }
                getNamesOf(hiuList)
            }
        })

        viewModel.consentListResponse.observe(this, Observer {
            when (it) {
                is Loading -> showProgressBar(it.isLoading, getString(R.string.loading_requests))
                is Success -> {
                    parentViewModel.showRefreshing(false)
                    viewModel.filterConsents(it.data?.requests)
                    binding.hideRequestsList = viewModel.grantedConsentsList.value.isNullOrEmpty()
                    binding.hideFilter = true
                }
                is PartialFailure -> {
                    context?.showAlertDialog(getString(R.string.failure), it.error?.message,
                        getString(string.ok))
                }
            }
        })

        viewModel.grantedConsentDetailsResponse.observe(
            this,
            Observer<PayloadResource<List<GrantedConsentDetailsResponse>>> { payload ->
                when (payload) {
                    is Success -> {
                        payload.data?.firstOrNull()?.consentDetail?.let {
                            viewModel.revokeConsent(it.id, context?.getConsentTempToken()!!)
                        }
                    }

                    is Loading -> {
                        showProgressBar(payload.isLoading)
                    }
                }
            })
        viewModel.revokeConsentResponse.observe(this, Observer<PayloadResource<Void>> {
            when (it) {
                is Loading -> showProgressBar(
                    it.isLoading,
                    getString(R.string.revoking_consent)
                )
                is Success -> {
                    activity?.let {
                    parentViewModel.pullToRefreshEvent.value = true
                    showShortToast("Consent revoked")
                    }
                }
                is PartialFailure -> {
                    context?.showAlertDialog(
                        getString(R.string.failure), it.error?.message,
                        getString(string.ok)
                    )
                }
                is Failure -> {
                    context?.showAlertDialog(
                        getString(R.string.failure), it.error?.message,
                        getString(string.ok)
                    )
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
            layout.simple_dropdown_item_1line, android.R.id.text1,
            viewModel.populateFilterItems(resources, ConsentFlow.GRANTED_CONSENTS)
        )
        binding.spRequestFilter.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spRequestFilter.setSelection(selectedPosition)
    }

    private fun initBindings() {
        binding.noNewConsentsMessage = getString(R.string.no_granted_consents)
        binding.listener = this
        binding.hideRequestsList = true
        binding.hideFilter = true
        showProgressBar(false)
        initSpinner(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.getConsents()
    }

    private fun renderConsentRequests(requests: List<Consent>, selectedSpinnerPosition: Int) {
        consentsListAdapter = ConsentsListAdapter(
            this@ConsentListFragment,
            requests, this@ConsentListFragment
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
            INDEX_ACTIVE -> filterRequests(viewModel.grantedConsentsList.value!!.filter {
                !isDateExpired(
                    it.permission.dataEraseAt
                ) && it.status != RequestStatus.DENIED
            })
            INDEX_EXPIRED -> filterRequests(viewModel.grantedConsentsList.value!!.filter {
                isDateExpired(
                    it.permission.dataEraseAt
                ) && it.status != RequestStatus.DENIED
            })
            INDEX_ALL -> filterRequests(viewModel.grantedConsentsList.value!!)
        }
    }


    private fun filterRequests(requests: List<Consent>) {
        (rvConsents.adapter as ConsentsListAdapter).updateData(requests)
    }

    private fun getNamesOf(hiuList: List<HipHiuIdentifiable>) {
        val hipHiuNameResponse = viewModel.fetchHipHiuNamesOf(hiuList)
        hipHiuNameResponse.observe(this, Observer {
            viewModel.grantedConsentsList.value?.let { consentList ->
                if(it.status) {
                    consentList.forEach { consent -> consent.hiu.name = it.nameMap[consent.hiu.getId()] ?: "" }
                    renderConsentRequests(consentList, binding.spRequestFilter.selectedItemPosition)
                }
            }
        })
    }

    override fun askForConsentPin(iDataBindingModel: IDataBindingModel) {
        consentToRevoke = (iDataBindingModel as Consent)
        val intent = Intent(context, PinVerificationActivity::class.java)
        startActivityForResult(intent, 701)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        if (iDataBindingModel is Consent) {
            val intent = Intent(context, ConsentDetailsActivity::class.java)
            intent.putExtra(CONSENT_FLOW, ConsentFlow.GRANTED_CONSENTS.ordinal)
            startActivity(intent)
            EventBus.getDefault().postSticky(iDataBindingModel.id)
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 701) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.getGrantedConsentDetails(consentToRevoke.id)
            }
        }
    }
}