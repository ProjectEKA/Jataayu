package `in`.org.projecteka.jataayu.consent.ui.activity



import `in`.org.projecteka.jataayu.consent.databinding.ConsentDetailsFragmentBinding
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.core.model.RequestStatus
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.android.material.chip.Chip
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
class ConsentDetailsFragment : BaseFragment(), ItemClickCallback{
    private lateinit var binding: ConsentDetailsFragmentBinding

    private val eventBusInstance = EventBus.getDefault()
    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val consent = eventBusInstance.getStickyEvent(Consent::class.java)
        binding.consent = consent
        binding.requestExpired = consent.status == RequestStatus.REQUESTED
        eventBusInstance.removeStickyEvent(consent)

        for (hiType in consent.hiTypes) {
            binding.cgRequestInfoTypes.addView(newChip(hiType.description))
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
        val chip = Chip(context)
        chip.text = description
        return chip
    }
}
