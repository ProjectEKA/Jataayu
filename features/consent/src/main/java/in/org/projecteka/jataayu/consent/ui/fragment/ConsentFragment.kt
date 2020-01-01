package `in`.org.projecteka.jataayu.consent.ui.fragment

import DividerItemDecorator
import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.ConsentFragmentBinding
import `in`.org.projecteka.jataayu.consent.model.ConsentsListResponse
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConsentFragment : BaseFragment(), ItemClickCallback {

    companion object {
        fun newInstance() = ConsentFragment()
    }

    private val viewModel: ConsentViewModel by sharedViewModel()
    private lateinit var binding: ConsentFragmentBinding
//
//
    private val consentObserver =
        Observer<ConsentsListResponse?> { patientDiscoveryResponse ->
            renderPatientAccounts()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConsentFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.consentsListResponse.observe(this, consentObserver)
        viewModel.getConsents()
    }


    private fun renderPatientAccounts() {
        binding.rvConsents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(
                this@ConsentFragment,
                viewModel?.consentsListResponse?.value?.consents!!
            )
            addItemDecoration(
                DividerItemDecorator(
                    ContextCompat.getDrawable(
                        context!!,
                        R.color.transparent
                    )!!
                )
            )
        }
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
    }
}