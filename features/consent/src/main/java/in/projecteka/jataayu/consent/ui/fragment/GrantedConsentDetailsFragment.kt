package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.GrantedConsentDetailsFragmentBinding
import `in`.projecteka.jataayu.consent.Cache.ConsentDataProviderCacheManager
import `in`.projecteka.jataayu.consent.viewmodel.GrantedConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HiType
import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.Links
import `in`.projecteka.jataayu.core.model.grantedconsent.GrantedConsentDetailsResponse
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_consent_details_edit.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GrantedConsentDetailsFragment : BaseFragment(), ItemClickCallback {

    protected lateinit var binding: GrantedConsentDetailsFragmentBinding

    protected val viewModel: GrantedConsentViewModel by sharedViewModel()

    protected lateinit var consent: Consent

    protected var hiTypeObjects = ArrayList<HiType>()

    protected val eventBusInstance: EventBus = EventBus.getDefault()
    private lateinit var consentId: String
    private lateinit var genericRecyclerViewAdapter: GenericRecyclerViewAdapter
    private var linkedAccounts: List<Links>? = null
    private lateinit var linkedAccountsAndCount: Pair<List<IDataBindingModel>, Int>
    private val compositeDisposable = CompositeDisposable()
    private var hipInfoTasksCount = 0


    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
    }

    companion object {
        fun newInstance() = GrantedConsentDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GrantedConsentDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun bindUI(consent: Consent) {

        with(binding) {
            this.consent = consent
            requestExpired = true
            cgRequestInfoTypes.removeAllViews()
        }

        eventBusInstance.postSticky(consent)

        if (hiTypeObjects.isEmpty()) createHiTypesFromConsent()

        hiTypeObjects.forEach { hiType ->
            if (hiType.isChecked) {
                binding.cgRequestInfoTypes.addView(newChip(hiType.type))
            }
        }
    }

    private fun createHiTypesFromConsent() {
        consent.hiTypes.forEach {
            hiTypeObjects.add(HiType(it, true))
        }
    }

    private fun newChip(description: String): Chip? =
        Chip(context, null, R.style.Chip_NonEditable).apply {
            text = description
        }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.granted_consent)
    }

    override fun onStop() {
        super.onStop()
        eventBusInstance.unregister(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)

        initObservers()

        if (viewModel.linkedAccountsResponse.value == null) {
            viewModel.getLinkedAccounts()
        }
    }

    private fun initObservers() {

        viewModel.linkedAccountsResponse.observe(
            this,
            Observer<PayloadResource<LinkedAccountsResponse>> { payload ->
                when (payload) {
                    is Loading -> showProgressBar(payload.isLoading)
                    is Success -> {
                        linkedAccounts = payload.data?.linkedPatient?.links
                        linkedAccounts?.map { it.hip.id }.let { ids ->
                            this.getHIPInfo(ids!!) {
                                viewModel.grantedConsentDetailsResponse.value?.let {
                                    when(it) {
                                        is Success -> it.data?.let { result -> onConsentDetailsResponseSuccess(result) }
                                        else -> {}
                                    }
                                } ?: kotlin.run {
                                    viewModel.getGrantedConsentDetails(consentId)
                                }
                            }
                        }
                    }
                }
            })

        viewModel.grantedConsentDetailsResponse.observe(
            this,
            Observer<PayloadResource<List<GrantedConsentDetailsResponse>>> { payload ->
                when (payload) {
                    is Success -> {
                        payload.data?.let { onConsentDetailsResponseSuccess(it) }
                    }
                    is Loading -> {
                        showProgressBar(payload.isLoading)
                    }
                }
            })
    }

    // Build adapter data and populate
    private fun populateLinkedAccounts(grantedConsents: List<GrantedConsentDetailsResponse>) {
        compositeDisposable.add(io.reactivex.Observable.just(viewModel)
            .map { it.getItems(grantedConsents, linkedAccounts) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                linkedAccountsAndCount = it
                genericRecyclerViewAdapter =
                    GenericRecyclerViewAdapter(linkedAccountsAndCount.first, this)
                rvLinkedAccounts.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = genericRecyclerViewAdapter
                    val dividerItemDecorator = DividerItemDecorator(
                        ContextCompat.getDrawable(
                            context!!,
                            R.color.transparent
                        )!!
                    )
                    addItemDecoration(dividerItemDecorator)
                }

                binding.tvProviders.text =
                    String.format(
                        context!!.getString(R.string.all_linked_providers_with_count),
                        linkedAccountsAndCount.second
                    )
            })
    }

    private fun onConsentDetailsResponseSuccess(result: List<GrantedConsentDetailsResponse>) {

        result.firstOrNull()?.consentDetail?.let {
            ConsentDataProviderCacheManager.providerMap[it.hiu.id]?.let { providerInfo ->
                this.consent = it
                this.consent.hiu.name = providerInfo.hip.name
                bindUI(this.consent)
                populateLinkedAccounts(result)
            } ?: kotlin.run {
                getHIPInfo(listOf(it.hiu.id)) {
                    onConsentDetailsResponseSuccess(result)
                }
            }
        }
    }

    private fun getHIPInfo(providerIds: List<String>, completion: () -> Unit) {

        ConsentDataProviderCacheManager.fetchHipInfo(providerIds, viewModel.getConsentRepository(),this, completion)
    }


    @Subscribe(sticky = true)
    public fun onConsentIdReceived(consentId: String) {
        this.consentId = consentId
    }
}
