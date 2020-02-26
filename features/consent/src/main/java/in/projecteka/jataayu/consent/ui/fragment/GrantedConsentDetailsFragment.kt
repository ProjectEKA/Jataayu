package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.GrantedConsentDetailsFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PayloadResource
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GrantedConsentDetailsFragment : BaseFragment(), ItemClickCallback {

    private lateinit var binding: GrantedConsentDetailsFragmentBinding

    private lateinit var consent: Consent

    private var hiTypeObjects = ArrayList<HiType>()

    private lateinit var linkedAccounts: List<Links?>

    private val eventBusInstance = EventBus.getDefault()

    private val viewModel: ConsentViewModel by sharedViewModel()

    private val consentArtifactResponseObserver = Observer<PayloadResource<ConsentArtifactResponse>> {
        when (it) {
            is Loading -> {
                showProgressBar(it.isLoading)
            }
            is Success -> {
                if (it.data?.consents?.isNotEmpty() == true) {
                    eventBusInstance.post(MessageEventType.CONSENT_GRANTED)
                    activity?.finish()
                }
            }
        }
    }

    private val linkedAccountsObserver = Observer<PayloadResource<LinkedAccountsResponse>> {
        when (it) {
            is Loading -> showProgressBar(it.isLoading)
            is Success -> {
                it.data?.linkedPatient?.links?.let { links ->
                    linkedAccounts = links
                    linkedAccounts.forEach { link -> link?.careContexts?.forEach { it.contextChecked = true } }
                }
            }
        }
    }

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {}

    companion object {
        fun newInstance() = GrantedConsentDetailsFragment()
    }

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

        viewModel.consentArtifactResponse.observe(this, consentArtifactResponseObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GrantedConsentDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun renderUi() {
        with(binding) {
            consent = this@GrantedConsentDetailsFragment.consent
            cgRequestInfoTypes.removeAllViews()
        }

        eventBusInstance.postSticky(consent)

        if (hiTypeObjects.isEmpty()) createHiTypesFromConsent()

        hiTypeObjects.forEach { if (it.isChecked) binding.cgRequestInfoTypes.addView(newChip(it.type)) }

    }

    private fun createHiTypesFromConsent() {
        for (hiType in consent.hiTypes) {
            hiTypeObjects.add(HiType(hiType, true))
        }
    }

    override fun onStop() {
        super.onStop()
        eventBusInstance.unregister(this)
    }

    private fun newChip(description: String): Chip? {
        val chip = Chip(context, null, R.style.Chip_NonEditable)
        chip.text = description
        return chip
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.new_request)
        renderUi()
    }

    @Subscribe(sticky = true)
    public fun onConsentReceived(consent: Consent) {
        this.consent = consent
    }

}
