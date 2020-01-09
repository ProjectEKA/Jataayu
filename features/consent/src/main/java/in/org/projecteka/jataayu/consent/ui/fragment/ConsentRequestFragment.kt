package `in`.org.projecteka.jataayu.consent.ui.fragment

import DividerItemDecorator
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
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.consent_request_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel



class ConsentRequestFragment : BaseFragment(), ItemClickCallback, AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            1 -> renderPatientAccounts((viewModel.requests).filter { it.status.equals(RequestStatus.REQUESTED) }, position)
            2 -> renderPatientAccounts((viewModel.requests).filter { it.status.equals(RequestStatus.EXPIRED)}, position)
            else ->
                renderPatientAccounts(viewModel.requests, position)
        }
    }

    companion object {
        fun newInstance() = ConsentRequestFragment()
    }

    private val viewModel: ConsentViewModel by sharedViewModel()
    private lateinit var binding: ConsentRequestFragmentBinding

    private val consentObserver = Observer<ConsentsListResponse?> { renderPatientAccounts(it?.requests!!, binding.spRequestFilter.selectedItemPosition) }

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
        binding.requestCount = getString(`in`.org.projecteka.jataayu.consent.R.string.all_requests, 0)
        binding.listener = this
        binding.hideRequestsList = true
        initSpinner(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.consentsListResponse.observe(this, consentObserver)
        viewModel.getConsents()
    }

    private fun renderPatientAccounts(requests : List<Consent>, selectedSpinnerPosition: Int) {
        binding.hideRequestsList = !viewModel.isRequestAvailable()
        rvConsents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(
                this@ConsentRequestFragment,
                requests
            )
            addItemDecoration(
                DividerItemDecorator(
                    ContextCompat.getDrawable(
                        context!!,
                        `in`.org.projecteka.jataayu.consent.R.color.transparent
                    )!!
                )
            )
        }
        initSpinner(selectedSpinnerPosition)
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        activity?.startActivity(Intent(getActivity(), ConsentDetailsActivity::class.java))
    }

}