package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.featuresprovider.databinding.ProviderSearchFragmentBinding
import `in`.org.projecteka.featuresprovider.databinding.ProviderSearchResultItemBinding
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.ui.adapter.ProviderSearchAdapter
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.org.projecteka.jataayu.util.extension.showShortToast
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.airasia.biglife.ui.utils.UiUtils
import org.koin.android.ext.android.inject

class ProviderSearchFragment : Fragment(), ItemClickCallback {

    companion object {
        fun newInstance() = ProviderSearchFragment()
    }

    private val viewModel: ProviderSearchViewModel by inject()
    private lateinit var binding: ProviderSearchFragmentBinding
    private lateinit var lastQuery: String
    private var providers: List<ProviderInfo> = ArrayList()

    private lateinit var providersList: ProviderSearchAdapter

    private val providersObserver = Observer<List<ProviderInfo>> { providers ->
        if (providers.isNotEmpty()) {
            providersList.updateData(
                lastQuery,
                providers
            )
        } else {
            providersList.updateData(
                lastQuery,
                noResultsFoundView()
            )
        }
    }

    private fun observeProviders() {
        viewModel.providers.observe(this, providersObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProviderSearchFragmentBinding.inflate(inflater)
        renderSearchUi()
        return binding.root
    }

    private fun renderSearchUi() {
        binding.svProvider.addTextChangedListener(textWatcher)
        binding.ivClearResults.setOnClickListener { binding.svProvider.text.clear() }
        binding.rvSearchResults.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        providersList = ProviderSearchAdapter(this)
        binding.rvSearchResults.adapter = providersList
        binding.rvSearchResults.addOnScrollListener(onScrollListener)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
            when {
                hasValidSearchQuery(query) -> {
                    viewModel.getProvider(query.toString())
                    lastQuery = query.toString()
                }
                else -> providersList.updateData(listOf())
            }
            binding.ivClearResults.visibility = when {
                query.isNullOrEmpty() -> View.GONE
                else -> View.VISIBLE
            }
        }

        private fun hasValidSearchQuery(query: CharSequence?) =
            query != null && query.trim().isNotEmpty() && query.length >= context?.resources?.getInteger(
                R.integer.autocomplete_threshold
            )!!
    }

    private fun noResultsFoundView() = listOf(
        ProviderInfo(
            city = "",
            name = "",
            telephone = "",
            type = ""
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProviders()
    }

    override fun performItemClickAction(iDataBinding: IDataBinding, binding: ViewDataBinding) {
        showShortToast((binding as ProviderSearchResultItemBinding).providerName.text)
    }

    private val onScrollListener =
        object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                UiUtils.hideKeyboard(context as Activity)
            }
        }
}
