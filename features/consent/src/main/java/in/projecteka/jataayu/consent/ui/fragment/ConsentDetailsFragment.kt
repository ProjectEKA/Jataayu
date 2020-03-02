package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.ConsentDetailsFragmentBinding
import `in`.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HiType
import `in`.projecteka.jataayu.network.utils.ResponseCallback
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
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class ConsentDetailsFragment : BaseFragment(), ItemClickCallback,
    ResponseCallback {

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

    protected fun renderUi() {

        binding.consent = consent

        binding.requestExpired = isExpiredOrGranted()

        binding.isGrantedConsent = isGrantedConsent()

        eventBusInstance.postSticky(consent)

        binding.cgRequestInfoTypes.removeAllViews()

        if (hiTypeObjects.isEmpty()) createHiTypesFromConsent()

        for (hiType in hiTypeObjects) {
            if (hiType.isChecked) {
                binding.cgRequestInfoTypes.addView(newChip(hiType.type))
            }
        }
    }

    abstract fun isExpiredOrGranted(): Boolean
    abstract fun isGrantedConsent(): Boolean

    private fun createHiTypesFromConsent() {
        for (hiType in consent.hiTypes) {
            hiTypeObjects.add(HiType(hiType, true))
        }
    }

    private fun newChip(description: String): Chip? {
        val chip = Chip(context, null, R.style.Chip_NonEditable)
        chip.text = description
        return chip
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.new_request)
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ResponseBody) {
        showProgressBar(false)
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
    }
}
