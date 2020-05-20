package `in`.projecteka.jataayu.provider.ui.fragment

import `in`.projecteka.featuresprovider.R
import `in`.projecteka.featuresprovider.databinding.ProviderSearchFragmentBinding
import `in`.projecteka.jataayu.core.model.Hip
import `in`.projecteka.jataayu.core.model.ProviderInfo
import `in`.projecteka.jataayu.core.model.Request
import `in`.projecteka.jataayu.core.model.UnverifiedIdentifier
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.provider.callback.TextWatcherCallback
import `in`.projecteka.jataayu.provider.domain.ProviderNameWatcher
import `in`.projecteka.jataayu.provider.model.PatientDiscoveryResponse
import `in`.projecteka.jataayu.provider.ui.ProviderActivity
import `in`.projecteka.jataayu.provider.ui.adapter.ProviderSearchAdapter
import `in`.projecteka.jataayu.provider.ui.handler.ProviderSearchScreenHandler
import `in`.projecteka.jataayu.provider.viewmodel.ProviderActivityViewModel
import `in`.projecteka.jataayu.provider.viewmodel.ProviderSearchViewModel
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.ui.UiUtils
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.provider_search_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProviderSearchFragment : BaseFragment(), ItemClickCallback, TextWatcherCallback,
    ProviderSearchScreenHandler, ResponseCallback {
    private lateinit var binding: ProviderSearchFragmentBinding

    companion object {
        fun newInstance() = ProviderSearchFragment()
        const val UNVERIFIED_IDENTIFIER_MEDICAL_RECORD = "MR"

    }
    private val viewModel: ProviderSearchViewModel by sharedViewModel()
    private val parentVM: ProviderActivityViewModel by sharedViewModel()
    private lateinit var lastQuery: String
    private lateinit var selectedProvider : ProviderInfo
    private lateinit var providersList: ProviderSearchAdapter

    private val providersObserver = Observer<List<ProviderInfo>> { providerNames ->
        providersList.updateData(lastQuery, providerNames)
        showNoResultsFoundView(providerNames.isEmpty())
    }

    private val patientAccountsObserver =
        Observer<PatientDiscoveryResponse?> { _ ->
            showPatientAccountsList()
        }

    private fun showPatientAccountsList() {
        (activity as ProviderActivity).showPatientsAccounts()
    }

    private fun observeProviders() {
        viewModel.providers.observe(this, providersObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProviderSearchFragmentBinding.inflate(inflater)
        initBindings()
        renderSearchUi()
        return binding.root
    }

    private fun initBindings() {

        binding.viewModel = viewModel
        binding.clearButtonVisibility = GONE
        binding.inEditMode = true
        binding.selectedProviderName = viewModel.selectedProviderName
        binding.clickHandler = this
        binding.textWatcher = ProviderNameWatcher(this, 1)
        binding.mobile = viewModel.preferenceRepository.mobileIdentifier
    }

    private fun renderSearchUi() {
        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)
        providersList = ProviderSearchAdapter(this)
        binding.rvSearchResults.adapter = providersList
        binding.rvSearchResults.addOnScrollListener(onScrollListener)
    }

    private fun showNoResultsFoundView(show: Boolean) {
        binding.noResultsFoundView.visibility = if (show) VISIBLE else GONE
    }

    override fun onVisible() {
        if (!binding.inEditMode!!)
            (activity as? ProviderActivity)?.updateTitle(getString(R.string.link_your_id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProviders()
        initObservers()
        parentVM.showSnackbarevent.observe(this, Observer{
            if (it) {
                showSnackbar(getString(R.string.registered_successfully))
            }
        })
        viewModel.fetchProfileData()
        setTitle(R.string.link_provider)
    }

    private fun showSnackbar(message: String) {
        val spannableString = SpannableString(message)

        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val snackbar = Snackbar.make(provider_container, spannableString, 2000)
        snackbar.anchorView = btn_search
        snackbar.show()
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        providersList.updateData(lastQuery, emptyList())
        binding.inEditMode = false
        UiUtils.hideKeyboard(activity as Activity)
        selectedProvider = iDataBindingModel as ProviderInfo
        binding.tvSelectedProvider.text = selectedProvider.nameCityPair()
        viewModel.selectedProviderName = selectedProvider.nameCityPair()
        binding.tvSearchProviderLabel.text = getString(R.string.we_will_be_sending_info_to)
        binding.svProvider.clearFocus()
        binding.tvSelectedProvider.postDelayed({ binding.tvSelectedProvider.requestFocus() }, 100)
        binding.btnSearch.text = getString(R.string.fetch_record)
    }

    private val onScrollListener =
        object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                UiUtils.hideKeyboard(context as Activity)
            }
        }

    override fun onTextChanged(changedText: CharSequence?, clearButtonVisibility: Int) {
        viewModel.getProviders(changedText.toString())
        lastQuery = changedText.toString()
        binding.clearButtonVisibility = VISIBLE
    }

    override fun onTextCleared(clearButtonVisibility: Int) {
        viewModel.clearList()
        providersList.updateData(listOf())
        binding.clearButtonVisibility = GONE
        showNoResultsFoundView(false)
    }

    override fun onClearTextButtonClick(view: View) {
        binding.svProvider.text.clear()
        binding.inEditMode = true
        updateViewDetailsVisibility()
    }

    override fun onClearSelectionClick(view: View) {
        binding.svProvider.setText(lastQuery)
        binding.svProvider.setSelection(lastQuery.length)
        binding.inEditMode = true
        updateViewDetailsVisibility()
        binding.svProvider.requestFocus()
        setTitle(R.string.link_provider)
        binding.btnSearch.text = getString(R.string.link_provider)
    }

    override fun onSearchButtonClick(view: View) {
        var unverifiedIdentifiers= ArrayList<UnverifiedIdentifier>()
        if (binding.etPatientId.text.toString().isNotEmpty()){
            unverifiedIdentifiers.add(UnverifiedIdentifier(binding.etPatientId.text.toString(), UNVERIFIED_IDENTIFIER_MEDICAL_RECORD))
        }
        viewModel.getPatientAccounts(Request(viewModel.uuidRepository.generateUUID(), Hip(selectedProvider.hip.getId(), selectedProvider.hip.name), unverifiedIdentifiers), this)
        viewModel.showProgress(true, R.string.looking_up_info)
        observePatients()
    }

    private fun observePatients() =
        viewModel.patientDiscoveryResponse.observe(this, patientAccountsObserver)

    private fun initObservers() {

        binding.viewDetails.setOnClickListener {
           updateViewDetailsVisibility()
        }
        viewModel.userProfileResponse.observe(this, Observer {
            when (it) {
                is Failure -> {
                    context?.showErrorDialog(it.error.localizedMessage)
                }
                is PartialFailure -> {
                    context?.showAlertDialog(
                        getString(R.string.failure), it.error?.message, getString(
                            android
                                .R.string.ok
                        )
                    )
                }
            }
        })

        viewModel.myProfile.observe(this, Observer { profile ->
            binding.setSex(
                if(profile.gender == "M") {
                    getString(R.string.male)
                } else {
                    getString(R.string.female)
                }
            )
            binding.setFullName(profile.name)
            profile.yearOfBirth?.let {
                binding.setYearOfBirth(it.toString())
            }?: kotlin.run {
                binding.yearOfBirth.visibility = GONE
                binding.yearOfBirthLbl.visibility = GONE
            }

            profile.unverifiedIdentifiers?.let { unverifiedIdentifiers ->
                unverifiedIdentifiers.firstOrNull { it.type == PreferenceRepository.TYPE_AYUSHMAN_BHARAT_ID }?.let {
                    binding.ayushmanId = it.value
                } ?: kotlin.run {
                    binding.ayushmanBharatId.visibility = GONE
                    binding.ayushmanBharatIdLbl.visibility = GONE
                }
            } ?: kotlin.run {
                binding.ayushmanBharatId.visibility = GONE
                binding.ayushmanBharatIdLbl.visibility = GONE
            }
        })
    }

    private fun updateViewDetailsVisibility() {
        if (binding.inEditMode == false) {
            viewModel.isViewDetailsEnabled.set(!viewModel.isViewDetailsEnabled.get())
        } else {
            viewModel.isViewDetailsEnabled.set(false)
        }

        if (viewModel.isViewDetailsEnabled.get()) {
            //right drawable
            val arrowTop = ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_up)
            binding.viewDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowTop, null)
        } else {
            val arrowRight = ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_right)
            binding.viewDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowRight, null)
        }
    }

    override fun <T> onSuccess(body: T?) {
        viewModel.showProgress(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        viewModel.showProgress(false)
        context?.showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        viewModel.showProgress(false)
        context?.showErrorDialog(t.localizedMessage)
    }
}