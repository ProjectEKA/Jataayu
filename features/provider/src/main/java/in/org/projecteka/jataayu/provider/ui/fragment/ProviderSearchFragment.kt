package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.databinding.ProviderSearchFragmentBinding
import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.provider.callback.TextWatcherCallback
import `in`.org.projecteka.jataayu.provider.domain.ProviderNameWatcher
import `in`.org.projecteka.jataayu.provider.model.ProviderInfo
import `in`.org.projecteka.jataayu.provider.ui.adapter.ProviderSearchAdapter
import `in`.org.projecteka.jataayu.provider.ui.handler.ProviderSearchScreenHandler
import `in`.org.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.org.projecteka.jataayu.util.extension.EMPTY
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

class ProviderSearchFragment : Fragment(), ItemClickCallback, TextWatcherCallback,
    ProviderSearchScreenHandler {

    companion object {
        fun newInstance() = ProviderSearchFragment()
    }

    private val viewModel : ProviderSearchViewModel by inject()
    private lateinit var binding : ProviderSearchFragmentBinding
    private lateinit var lastQuery : String
    private var selectedProviderName = String.EMPTY

    private lateinit var providersList : ProviderSearchAdapter

    private val providersObserver = Observer<List<ProviderInfo>> { providerNames ->
        if (providerNames.isNotEmpty()) {
            providersList.updateData(
                lastQuery, providerNames)
            setNoResultsFoundViewVisibility(GONE)
        } else {
            providersList.updateData(lastQuery, emptyList())
            setNoResultsFoundViewVisibility(VISIBLE)
        }
    }

    private fun observeProviders() {
        viewModel.providers.observe(this, providersObserver)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View {
        binding = ProviderSearchFragmentBinding.inflate(inflater)
        initBindings()
        renderSearchUi()
        return binding.root
    }

    private fun initBindings() {
        binding.clearButtonVisibility = GONE
        binding.inEditMode = true
        binding.selectedProviderName = selectedProviderName
        binding.clickHandler = this
        binding.textWatcher = ProviderNameWatcher(this, 1)
    }

    private fun renderSearchUi() {
        binding.tvSelectedProvider.setOnClickListener {
            binding.inEditMode = true
            binding.svProvider.setText(lastQuery)
            binding.svProvider.setSelection(lastQuery.length)
            binding.svProvider.requestFocus()
        }
        binding.ivClearResults.setOnClickListener {
            binding.svProvider.apply {
                text.clear()
                binding.inEditMode = true
            }
        }
        binding.rvSearchResults.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        providersList = ProviderSearchAdapter(this)
        binding.rvSearchResults.adapter = providersList
        binding.rvSearchResults.addOnScrollListener(onScrollListener)
    }

    private fun setNoResultsFoundViewVisibility(visible : Int) {
        binding.noResultsFoundView.visibility = visible
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProviders()
    }

    override fun performItemClickAction(iDataBinding : IDataBinding,
                                        itemViewBinding : ViewDataBinding) {
        providersList.updateData(lastQuery, emptyList())
        binding.inEditMode = false
        UiUtils.hideKeyboard(activity as Activity)
        binding.tvSelectedProvider.text = (iDataBinding as ProviderInfo).nameCityPair()
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
        setNoResultsFoundViewVisibility(GONE)
    }

    override fun onClearTextButtonClick(view: View) {

    }

    override fun onClearSelectionButtonClick(view: View) {

    }

    override fun onSearchButtonClick(view: View) {
    }
}