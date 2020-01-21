package `in`.org.projecteka.jataayu.consent.ui.fragment

import DividerItemDecorator
import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.ConsentRequestFragmentBinding
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.core.model.RequestStatus
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.util.extension.startActivity
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



class RequestListFragment : BaseFragment(), ItemClickCallback, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ConsentRequestFragmentBinding

    companion object {
        fun newInstance() = RequestListFragment()

    }
    private val viewModel: ConsentViewModel by sharedViewModel()

    private val consentObserver = Observer<ConsentsListResponse?> { renderConsentRequests(it?.requests!!, binding.spRequestFilter.selectedItemPosition) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentRequestFragmentBinding.inflate(inflater)
        initBindings()
        return binding.root
    }

    private fun initSpinner(selectedPosition: Int) {
        val arrayAdapter =
            ArrayAdapter<String>(context!!, android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                viewModel.populateFilterItems(resources)
            )
        binding.spRequestFilter.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spRequestFilter.setSelection(selectedPosition)
    }

    private fun initBindings() {
        binding.requestCount = getString(R.string.all_requests, 0)
        binding.listener = this
        binding.hideRequestsList = true
        binding.progressBarVisibility = View.GONE
        initSpinner(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.consentsListResponse.observe(this, consentObserver)
        viewModel.getConsents()
        binding.progressBarVisibility = View.VISIBLE
    }

    private fun renderConsentRequests(requests : List<Consent>, selectedSpinnerPosition: Int) {
        hideRequestLoading()
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
    }

    private fun hideRequestLoading() {
        binding.progressBarVisibility = View.GONE
    }

    override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
        startActivity(ConsentDetailsActivity::class.java)
        EventBus.getDefault().postSticky(iDataBindingModel as Consent)
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            1 -> renderConsentRequests((viewModel.requests).filter { it.status.equals(RequestStatus.REQUESTED) }, position)
            2 -> renderConsentRequests((viewModel.requests).filter { it.status.equals(RequestStatus.EXPIRED)}, position)
            else ->
                renderConsentRequests(viewModel.requests, position)
        }
    }
}