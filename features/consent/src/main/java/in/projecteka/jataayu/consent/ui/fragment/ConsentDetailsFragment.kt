package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.ConsentDetailsFragmentBinding
import `in`.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.approveconsent.ConsentArtifactResponse
import `in`.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.projecteka.jataayu.network.utils.*
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.provider.ui.handler.ConsentDetailsClickHandler
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
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

class ConsentDetailsFragment : BaseFragment(), ItemClickCallback, ConsentDetailsClickHandler {

    companion object {
        fun newInstance() = ConsentDetailsFragment()
    }

    private lateinit var binding: ConsentDetailsFragmentBinding

    private lateinit var consent: Consent

    private var hiTypeObjects = ArrayList<HiType>()

    private lateinit var linkedAccounts: List<Links>

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
                    linkedAccounts.forEach { link -> link.careContexts.forEach { it.contextChecked = true } }
                }
            }
        }
    }


    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {

    }

    override fun onEditClick(view: View) {
        linkedAccounts.let {
            eventBusInstance.postSticky(HiTypeAndLinks(hiTypeObjects, linkedAccounts))
            (activity as ConsentDetailsActivity).editConsentDetails()
        }
    }

    override fun onDenyConsent(view: View) {
        activity?.finish()
    }

    override fun onGrantConsent(view: View) {
        if (linkedAccounts.isNotEmpty()) {
            showProgressBar(true)
            viewModel.grantConsent(consent.id, viewModel.getConsentArtifact(linkedAccounts, hiTypeObjects, consent.permission))
        }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ConsentDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun renderUi() {
        with(binding) {
            consent = this@ConsentDetailsFragment.consent
            requestExpired = DateTimeUtils.isDateExpired(this@ConsentDetailsFragment.consent.permission.dataExpiryAt)
            cgRequestInfoTypes.removeAllViews()
        }

        eventBusInstance.postSticky(consent)


        if (hiTypeObjects.isEmpty()) createHiTypesFromConsent()

        for (hiType in hiTypeObjects) {
            if (hiType.isChecked) {
                binding.cgRequestInfoTypes.addView(newChip(hiType.type))
            }
        }

        binding.clickHandler = this
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
