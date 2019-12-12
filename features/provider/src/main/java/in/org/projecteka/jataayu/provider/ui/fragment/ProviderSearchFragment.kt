package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.databinding.ProviderSearchFragmentBinding
import `in`.org.projecteka.featuresprovider.databinding.ProviderSearchResultItemBinding
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.provider.callback.TextWatcherCallback
import `in`.org.projecteka.jataayu.provider.domain.ProviderNameWatcher
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.ui.adapter.ProviderSearchAdapter
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.org.projecteka.jataayu.util.extension.showShortToast
import `in`.org.projecteka.jataayu.util.ui.UiUtils
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject

class ProviderSearchFragment : Fragment(), ItemClickCallback, TextWatcherCallback {

    companion object {
        fun newInstance() = ProviderSearchFragment()
    }

    private val viewModel : ProviderSearchViewModel by inject()
    private lateinit var binding : ProviderSearchFragmentBinding
    private lateinit var lastQuery : String

    private lateinit var providersList : ProviderSearchAdapter

    private val providersObserver = Observer<List<ProviderInfo>> { providers ->
        if (providers.isNotEmpty()) {
            providersList.updateData(
                lastQuery, providers)
        } else {
            providersList.updateData(
                lastQuery, noResultsFoundView())
        }
    }

    private fun observeProviders() {
        viewModel.providers.observe(this, providersObserver)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View {
        binding = ProviderSearchFragmentBinding.inflate(inflater)
        binding.clearButtonVisibility = GONE
        binding.textWatcher = ProviderNameWatcher(this, 1)
        renderSearchUi()
        return binding.root
    }

    private fun renderSearchUi() {
        binding.ivClearResults.setOnClickListener { binding.svProvider.text.clear() }
        binding.rvSearchResults.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        providersList = ProviderSearchAdapter(this)
        binding.rvSearchResults.adapter = providersList
        binding.rvSearchResults.addOnScrollListener(onScrollListener)
    }

    private fun noResultsFoundView() =
        listOf(ProviderInfo(city = "", name = "", telephone = "", type = ""))

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProviders()
    }

    override fun performItemClickAction(iDataBinding : IDataBinding, binding : ViewDataBinding) {
        showShortToast((binding as ProviderSearchResultItemBinding).providerName.text)
    }

    private val onScrollListener =
        object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView : androidx.recyclerview.widget.RecyclerView, newState : Int) {
                super.onScrollStateChanged(recyclerView, newState)
                UiUtils.hideKeyboard(context as Activity)
            }
        }

    override fun onTextChanged(changedText : CharSequence?, clearButtonVisibility : Int) {
        viewModel.getProvider(changedText.toString())
        lastQuery = changedText.toString()
        binding.clearButtonVisibility = VISIBLE
    }

    override fun onTextCleared(clearButtonVisibility : Int) {
        providersList.updateData(listOf())
        binding.clearButtonVisibility = GONE
    }
}