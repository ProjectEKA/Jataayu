package `in`.org.projecteka.jataayu.consent.ui.fragment



import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.ConsentDetailsFragmentBinding
import `in`.org.projecteka.jataayu.consent.ui.activity.ConsentDetailsActivity
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.core.model.HiType
import `in`.org.projecteka.jataayu.core.model.RequestStatus
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.provider.ui.handler.ConsentDetailsClickHandler
import `in`.org.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.android.material.chip.Chip
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ConsentDetailsFragment : BaseFragment(), ItemClickCallback, ConsentDetailsClickHandler {
    override fun onGrantConsentClick(view: View) {
        (activity as ConsentDetailsActivity).grantRequest()
    }

    private lateinit var binding: ConsentDetailsFragmentBinding

    private lateinit var consent: Consent

    private var hiTypeObjects = ArrayList<HiType>()

    private val eventBusInstance = EventBus.getDefault()

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding) {
    }
    override fun onEditClick(view: View) {
        (activity as ConsentDetailsActivity).editConsentDetails()
    }

    private val viewModel: ConsentViewModel by sharedViewModel()
    companion object{
        fun newInstance() = ConsentDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConsentDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun renderUi() {
        consent = eventBusInstance.getStickyEvent(Consent::class.java)

        Timber.d("hityes ${hiTypeObjects.size}")

        eventBusInstance.removeStickyEvent(Consent::class.java)

        binding.consent = consent
        binding.requestExpired = consent.status == RequestStatus.REQUESTED

        eventBusInstance.postSticky(consent)

        binding.cgRequestInfoTypes.removeAllViews()

        if(hiTypeObjects.isEmpty()) createHitypesFromConsent()

        for (hiType in hiTypeObjects) {
            if (hiType.isChecked){
                binding.cgRequestInfoTypes.addView(newChip(hiType.type))
            }
        }

        eventBusInstance.postSticky(hiTypeObjects)

        binding.consentDetailsClickHandler = this
    }

    private fun createHitypesFromConsent() {
        for (hiType in consent.hiTypes) {
            hiTypeObjects.add(HiType(hiType, true))
        }
    }

    @Subscribe
    public fun onConsentReceived(consent: Consent) {}

    override fun onStart() {
        super.onStart()
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)
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


    @Subscribe
    public fun onHiTypesReceived(hiTypes: ArrayList<HiType>) {

    }

}
