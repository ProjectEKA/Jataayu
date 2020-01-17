
package `in`.org.projecteka.jataayu.consent.ui.activity

import `in`.org.projecteka.jataayu.consent.databinding.FragmentConsentDetailsEditBinding
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class EditConsentDetailsFragment : BaseFragment() {

    private val eventBusInstance: EventBus = EventBus.getDefault()
    private lateinit var binding: FragmentConsentDetailsEditBinding


    companion object {
        fun newInstance() = EditConsentDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsentDetailsEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val consent = eventBusInstance.getStickyEvent(Consent::class.java)
        binding.consent = consent

        for (hiType in consent.hiTypes) {
            binding.cgRequestInfoTypes.addView(newChip(hiType.description))
        }
    }

    private fun newChip(description: String): Chip? {
        val chip = Chip(context)
        chip.text = description
        return chip
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

}
