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
import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
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
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

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
        const val REQUEST_CODE_PIN_VERIFICATION = 701
        const val KEY_SCOPE_TYPE = "scope_type"
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

        viewModel.consentArtifactResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Loading -> {
                    if (viewModel.isLoadingMore.get() != View.VISIBLE) {
                        viewModel.showProgress(response.isLoading, R.string.loading_requests)
                    } else {
                        viewModel.isLoadingMore.set(View.VISIBLE)
                    }
                }
                is Success -> {
                    response.data?.let {
                        viewModel.updateConsentArtifactList(it)
                        val hiuList = it.getArtifacts().map { consent -> consent.hiu }
                        getNamesOf(hiuList)
                        binding.noNewConsentsMessage = getString(viewModel.getNoConsentMessage())
                        binding.hideFilter = false
                        binding.hideRequestsList = it.getArtifacts().isNullOrEmpty()
                    }
                    parentViewModel.showRefreshing(false)
                    viewModel.isLoadingMore.set(View.INVISIBLE)
                    viewModel.showProgress(false)
                }
                is PartialFailure -> {
                    context?.showAlertDialog(getString(R.string.failure), response.error?.message,
                        getString(string.ok))
                }
            }
        })

        viewModel.revokeConsentResponse.observe(viewLifecycleOwner, Observer<PayloadResource<Void>> {
            when (it) {
                is Loading -> viewModel.showProgress(
                    it.isLoading, R.string.revoking_consent
                )
                is Success -> {
                    activity?.let {
                    parentViewModel.pullToRefreshEvent.value = true
                    parentViewModel.showToastEvent.value = getString(R.string.consent_revoked)
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
                        getString(R.string.failure), it.error.message,
                        getString(string.ok)
                    )
                }

            }
        })

        viewModel.currentStatus.observe(viewLifecycleOwner, Observer {
            clearRecylerView()
        })

        parentViewModel.pullToRefreshEvent.observe(this, Observer{
            if (it) {
                clearRecylerView()
            }
        })
    }

    private fun initSpinner(selectedPosition: Int) {
        val arrayAdapter = ArrayAdapter<String>(
            context!!,
            layout.simple_dropdown_item_1line, android.R.id.text1,
            viewModel.populateFilterItems(resources)
        )
        binding.spRequestFilter.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spRequestFilter.setSelection(selectedPosition)
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.listener = this
        binding.hideRequestsList = true
        binding.hideFilter = true
        initSpinner(0)
        initRecyclerViewAdapter()
    }

    private fun initRecyclerViewAdapter() {
        consentsListAdapter = ConsentsListAdapter(
            this, listOf(), this
        )
        binding.rvConsents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = consentsListAdapter
            addItemDecoration(DividerItemDecorator(getDrawable(context!!, R.color.transparent)!!))
            setupScrollListener()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.getConsents(offset = 0)
    }

    private fun renderConsentRequests(requests: List<Consent>, selectedSpinnerPosition: Int) {
        consentsListAdapter.updateData(requests)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.updateFilterSelectedItem(position)
    }


    private fun getNamesOf(hiuList: List<HipHiuIdentifiable>) {
        val hipHiuNameResponse = viewModel.fetchHipHiuNamesOf(hiuList)
        hipHiuNameResponse.observe(this, Observer {
            viewModel.consentArtifactList.value?.getArtifacts()?.let { consentList ->
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
        intent.putExtra(KEY_SCOPE_TYPE, ConsentScopeType.SCOPE_REVOKE.ordinal)
        startActivityForResult(intent, REQUEST_CODE_PIN_VERIFICATION)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        if (iDataBindingModel is Consent) {
            val intent = Intent(context, ConsentDetailsActivity::class.java)
            intent.putExtra(CONSENT_FLOW, ConsentFlow.GRANTED_CONSENTS.ordinal)
            startActivity(intent)
            EventBus.getDefault().postSticky(iDataBindingModel)
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PIN_VERIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.revokeConsent(consentToRevoke.id)
            }
        }
    }


    private fun clearRecylerView() {
        consentsListAdapter.clearAll()
        viewModel.getConsents(offset = 0)
    }

    private fun setupScrollListener() {
        binding.rvConsents.addOnScrollListener(viewModel.paginationScrollListener)
    }


}