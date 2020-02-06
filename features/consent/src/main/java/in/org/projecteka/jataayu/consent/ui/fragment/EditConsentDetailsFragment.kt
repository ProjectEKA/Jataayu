package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.FragmentConsentDetailsEditBinding
import `in`.org.projecteka.jataayu.consent.ui.handler.PickerClickHandler
import `in`.org.projecteka.jataayu.consent.viewmodel.ConsentViewModel
import `in`.org.projecteka.jataayu.core.databinding.PatientAccountResultItemBinding
import `in`.org.projecteka.jataayu.core.model.CareContext
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.core.model.HiType
import `in`.org.projecteka.jataayu.core.model.Links
import `in`.org.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.DateTimeSelectionCallback
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.presentation.ui.fragment.DatePickerDialog
import `in`.org.projecteka.jataayu.presentation.ui.fragment.DatePickerDialog.Companion.UNDEFINED_DATE
import `in`.org.projecteka.jataayu.presentation.ui.fragment.TimePickerDialog
import `in`.org.projecteka.jataayu.provider.ui.handler.EditConsentClickHandler
import `in`.org.projecteka.jataayu.util.extension.setTitle
import `in`.org.projecteka.jataayu.util.extension.showLongToast
import `in`.org.projecteka.jataayu.util.extension.toUtc
import `in`.org.projecteka.jataayu.util.ui.DateTimeUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_consent_details_edit.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.fragment_consent_details_edit.cg_request_info_types as chipGroup


class EditConsentDetailsFragment : BaseFragment(), PickerClickHandler, DateTimeSelectionCallback,
    EditConsentClickHandler, ItemClickCallback, ProgressDialogCallback {

    private lateinit var binding: FragmentConsentDetailsEditBinding
    lateinit var listItems: List<IDataBindingModel>
    lateinit var linkedAccounts: List<Links?>

    private val eventBusInstance: EventBus = EventBus.getDefault()
    lateinit var consent: Consent
    private lateinit var modifiedConsent: Consent
    private var hiTypes = ArrayList<HiType>()
    private val viewModel: ConsentViewModel by sharedViewModel()
    private lateinit var genericRecyclerViewAdapter: GenericRecyclerViewAdapter

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
        consent = eventBusInstance.getStickyEvent(Consent::class.java)

        modifiedConsent = consent.clone()
        binding.consent = modifiedConsent
        binding.clickHandler = this
        binding.pickerClickHandler = this
    }

    private fun renderUi() {
        for (i in 0 until hiTypes.size) {
            binding.cgRequestInfoTypes.addView(newChip(hiTypes[i].type, hiTypes[i].isChecked))
        }

        binding.cgRequestInfoTypes.setOnCheckedChangeListener { group, checkedId ->
            val chip = chipGroup.findViewById<Chip>(checkedId)
            if (chip != null) {
                showLongToast(chip.text.toString())
            }
        }
    }

    override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
        if (itemViewBinding is PatientAccountResultItemBinding) { //Check if it header or item
            val checkbox = itemViewBinding.cbCareContext
            checkbox.toggle()
            (iDataBindingModel as CareContext).contextChecked = checkbox.isChecked

            checkProviderSelection()
        }
    }

    private fun checkProviderSelection() {
        Thread {
            kotlin.run {
                var selectionCount = 0
                var selectableItemsCount = 0
                genericRecyclerViewAdapter.listOfBindingModels?.forEach {
                    if (it is CareContext) {
                        selectableItemsCount++
                        if (it.contextChecked) selectionCount++
                    }
                }

                binding.allProvidersChecked = selectableItemsCount == selectionCount
                binding.saveEnabled = selectionCount > 0
            }
        }.start()
    }

    private fun renderLinkedAccounts(linkedAccounts: List<Links?>) {
        listItems = viewModel.getItems(linkedAccounts)
        this.linkedAccounts = linkedAccounts
        genericRecyclerViewAdapter = GenericRecyclerViewAdapter(this@EditConsentDetailsFragment, listItems)
        rvLinkedAccounts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = genericRecyclerViewAdapter
            val dividerItemDecorator = DividerItemDecorator(ContextCompat.getDrawable(context!!, R.color.transparent)!!)
            addItemDecoration(dividerItemDecorator)
        }
    }

    override fun onTimeSelected(timePair: Pair<Int, Int>) {
        val calendar = Calendar.getInstance()
        calendar.time = DateTimeUtils.getDate(modifiedConsent.permission.dataExpiryAt)!!
        calendar.set(Calendar.HOUR_OF_DAY, timePair.first)
        calendar.set(Calendar.MINUTE, timePair.second)
        modifiedConsent.permission.dataExpiryAt = calendar.time.toUtc()
        binding.consent = modifiedConsent
    }

    override fun onDateSelected(@IdRes datePickerId: Int, date: Date) {
        when (datePickerId) {
            R.id.tv_requests_info_from -> modifiedConsent.updateFromDate(date.toUtc())
            R.id.tv_requests_info_to -> modifiedConsent.updateToDate(date.toUtc())
            R.id.tv_expiry_date -> modifiedConsent.permission.dataExpiryAt = date.toUtc()
        }
        binding.consent = modifiedConsent
    }

    override fun onTimePickerClick(view: View) {
        TimePickerDialog(modifiedConsent.getConsentExpiryTime(), this).show(
            fragmentManager!!,
            modifiedConsent.getConsentExpiryTime()
        )
    }

    override fun onDatePickerClick(view: View) {
        when (view.id) {
            R.id.tv_requests_info_from -> {
                val from = DateTimeUtils.getDate(modifiedConsent.permission.dateRange.from)?.time!!
                val datePickerDialog =
                    DatePickerDialog(R.id.tv_requests_info_from, from, UNDEFINED_DATE, System.currentTimeMillis(), this)
                datePickerDialog.show(fragmentManager!!, modifiedConsent.permission.dateRange.from)
            }
            R.id.tv_requests_info_to -> {
                val to = DateTimeUtils.getDate(modifiedConsent.permission.dateRange.to)?.time!!
                val from = DateTimeUtils.getDate(modifiedConsent.permission.dateRange.from)?.time!!
                DatePickerDialog(R.id.tv_requests_info_to, to, from, System.currentTimeMillis(), this).show(
                    fragmentManager!!,
                    modifiedConsent.permission.dateRange.to
                )
            }
            R.id.tv_expiry_date -> {
                DatePickerDialog(
                    R.id.tv_expiry_date, DateTimeUtils.getDate(modifiedConsent.permission.dataExpiryAt)?.time!!,
                    System.currentTimeMillis(), UNDEFINED_DATE, this
                ).show(
                    fragmentManager!!,
                    modifiedConsent.permission.dataExpiryAt
                )
            }
        }
    }

    private fun newChip(description: String, isChecked: Boolean): Chip? {
        val chip = Chip(context)
        chip.text = description
        chip.isChecked = isChecked
        return chip
    }

    @Subscribe
    public fun onConsentReceived(consent: Consent) {
    }

    @Subscribe(sticky = true)
    public fun onHitypesAndLinkesReceived(hiTypeAndLinks: HiTypeAndLinks) {
        hiTypes = hiTypeAndLinks.hiTypes
        renderLinkedAccounts(hiTypeAndLinks.linkedAccounts)
        renderUi()
        checkProviderSelection()
    }

    override fun onStart() {
        super.onStart()
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventBusInstance.unregister(this)
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.edit_request)
    }

    override fun onBackPressedCallback() {
        super.onBackPressedCallback()
        binding.consent = consent
    }

    override fun toggleProvidersSelection(view: View) {
        val checked = (view as CheckBox).isChecked
        listItems.forEach { if (it is CareContext) it.contextChecked = checked }

        rvLinkedAccounts.adapter?.notifyDataSetChanged()
        checkProviderSelection()
    }

    override fun onSaveClick(view: View) {
        hiTypes.forEach { it.isChecked = (chipGroup.getChildAt(hiTypes.indexOf(it)) as Chip).isChecked }

        eventBusInstance.postSticky(modifiedConsent)
        eventBusInstance.post(linkedAccounts)
        activity?.onBackPressed()
    }

    override fun onSuccess(any: Any?) {
        showProgressBar(false)
    }

    override fun onFailure(any: Any?) {
        showProgressBar(false)
    }
}
