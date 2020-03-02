package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.ConsentDetailsFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HiType
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.google.android.material.chip.Chip
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class ConsentDetailsFragment : BaseFragment(), ItemClickCallback{

    protected lateinit var binding: ConsentDetailsFragmentBinding

    protected val viewModel: ConsentViewModel by sharedViewModel()

    protected lateinit var consent: Consent

    protected var hiTypeObjects = ArrayList<HiType>()

    protected val eventBusInstance: EventBus = EventBus.getDefault()

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConsentDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    abstract fun isExpiredOrGranted(): Boolean
    abstract fun isGrantedConsent(): Boolean

    protected fun renderUi() {

        with(binding) {
            this.consent = this@ConsentDetailsFragment.consent
            requestExpired = isExpiredOrGranted()
            isGrantedConsent = isGrantedConsent()
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
        setTitle(R.string.new_request)
    }

}
