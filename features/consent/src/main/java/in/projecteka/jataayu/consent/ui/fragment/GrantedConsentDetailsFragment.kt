package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.GrantedConsentDetailsFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.GrantedConsentDetailsViewModel
import `in`.projecteka.jataayu.core.model.*
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

    protected val viewModel: GrantedConsentDetailsViewModel by sharedViewModel()

    protected lateinit var consent: Consent

    protected var hiTypeObjects = ArrayList<HiType>()

    protected val eventBusInstance: EventBus = EventBus.getDefault()
    private lateinit var consentId: String
    private lateinit var genericRecyclerViewAdapter: GenericRecyclerViewAdapter
    private var linkedAccounts: List<Links>? = null
    private lateinit var linkedAccountsAndCount: Pair<List<IDataBindingModel>, Int>
    private val compositeDisposable = CompositeDisposable()
    private var linkedAccountHipResponse = HipHiuNameResponse(false, hashMapOf())

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

        binding.viewModel = viewModel
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
                    is Loading -> viewModel.showProgress(payload.isLoading)
                    is Success -> {
                        linkedAccounts = payload.data?.linkedPatient?.links
                        linkedAccounts?.map { it.hip }?.let { ids ->
                            this.getHIPHiuNamesOf(ids) {
                                linkedAccountHipResponse = it
                                viewModel.grantedConsentDetailsResponse.value?.let { response ->
                                    when(response) {
                                        is Success -> response.data?.let { result -> onConsentDetailsResponseSuccess(result,it) }
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
                        payload.data?.let { onConsentDetailsResponseSuccess(it, null) }
                    }
                    is Loading -> {
                        viewModel.showProgress(payload.isLoading)
                    }
                }
            })
    }

    private fun populateLinkedAccounts(grantedConsents: List<GrantedConsentDetailsResponse>) {
        compositeDisposable.add(io.reactivex.Observable.just(viewModel)
            .map { it.getItems(grantedConsents, linkedAccounts, linkedAccountHipResponse) }
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

    private fun onConsentDetailsResponseSuccess(result: List<GrantedConsentDetailsResponse>, hipHiuNameResponse: HipHiuNameResponse?) {

        result.firstOrNull()?.consentDetail?.let { consent ->
            hipHiuNameResponse?.nameMap?.let { it[consent.hiu.getId()]}?.let { name ->
                this.consent = consent
                this.consent.hiu.name = name
                bindUI(this.consent)
                populateLinkedAccounts(result)
            } ?: kotlin.run {
                getHIPHiuNamesOf(listOf(consent.hiu)) { hipHiuNameResponse ->
                    onConsentDetailsResponseSuccess(result, hipHiuNameResponse)
                }
            }
        }
    }

    private fun getHIPHiuNamesOf(list: List<HipHiuIdentifiable>, completion: (HipHiuNameResponse) -> Unit) {

        val hipHiuNameResponse = viewModel.fetchHipHiuNamesOf(list)
        hipHiuNameResponse.observe(this, Observer {
            completion(it)
        })
    }


    @Subscribe(sticky = true)
    public fun onConsentIdReceived(consentId: String) {
        this.consentId = consentId
    }
}
