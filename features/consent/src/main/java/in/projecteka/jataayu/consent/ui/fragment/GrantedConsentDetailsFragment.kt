package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
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
import `in`.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.consent_details_fragment.*
import org.greenrobot.eventbus.Subscribe

class GrantedConsentDetailsFragment : ConsentDetailsFragment(), ItemClickCallback {


    private lateinit var consentId: String
    private lateinit var genericRecyclerViewAdapter: GenericRecyclerViewAdapter
    private var linkedAccounts: List<Links>? = null
    private lateinit var linkedAccountsAndCount: Pair<List<IDataBindingModel>, Int>
    private val compositeDisposable = CompositeDisposable()

    private val grantedConsentDetailsObserver by lazy {
        Observer<PayloadResource<List<GrantedConsentDetailsResponse>>> { payload ->
            when (payload) {
                is Success -> {
                    payload.data?.firstOrNull()?.consentDetail?.let { this.consent = it }
                    renderUi()
                    payload.data?.let { populateLinkedAccounts(it) }
                }
                is Loading -> {
                    showProgressBar(payload.isLoading)
                }
            }
        }
    }

    private val linkedAccountsObserver by lazy {
        Observer<PayloadResource<LinkedAccountsResponse>> { payload ->
            when (payload) {
                is Loading -> showProgressBar(payload.isLoading)
                is Success -> {
                    linkedAccounts = payload.data?.linkedPatient?.links
                    if (viewModel.grantedConsentDetailsResponse.value == null) {
                        viewModel.getGrantedConsentDetails(consentId)
                    }
                }
            }
        }
    }

    private fun populateLinkedAccounts(grantedConsents: List<GrantedConsentDetailsResponse>) {
        compositeDisposable.add(io.reactivex.Observable.just(viewModel)
            .map { it.getItems(grantedConsents, linkedAccounts) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                linkedAccountsAndCount = it
                genericRecyclerViewAdapter = GenericRecyclerViewAdapter(linkedAccountsAndCount.first, this)
                rvLinkedAccounts.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = genericRecyclerViewAdapter
                    val dividerItemDecorator = DividerItemDecorator(ContextCompat.getDrawable(context!!, R.color.transparent)!!)
                    addItemDecoration(dividerItemDecorator)
                }

                binding.tvProviders.text =
                    String.format(context!!.getString(R.string.all_linked_providers_with_count), linkedAccountsAndCount.second)
            })
    }

    companion object {
        fun newInstance() = GrantedConsentDetailsFragment()
    }

    override fun onStop() {
        super.onStop()
        eventBusInstance.unregister(this)
    }

    override fun isExpiredOrGranted(): Boolean {
        return true
    }

    override fun isGrantedConsent(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)

        initObservers()

        if (viewModel.linkedAccountsResponse.value == null) {
            viewModel.getLinkedAccounts()
        }
    }

    private fun initObservers() {
        viewModel.linkedAccountsResponse.observe(this, linkedAccountsObserver)

        viewModel.grantedConsentDetailsResponse.observe(this, grantedConsentDetailsObserver)

    }

    @Subscribe(sticky = true)
    public fun onConsentIdReceived(consentId: String) {
        this.consentId = consentId
    }

    override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.granted_consent)
    }
}
