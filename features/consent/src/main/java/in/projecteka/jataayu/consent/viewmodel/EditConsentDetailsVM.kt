package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.HiType
import `in`.projecteka.jataayu.core.model.Links
import `in`.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.projecteka.jataayu.presentation.callback.DateTimeSelectionCallback
import `in`.projecteka.jataayu.util.extension.toUtc
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class EditConsentDetailsVM(private val repository: ConsentRepository) : ViewModel(), DateTimeSelectionCallback {

    companion object {
        const val FROM_DATE_DATEPICKER_ID = 0
        const val TO_DATE_DATEPICKER_ID = 1
        const val EXPIRY_DATE_DATEPICKER_ID = 2
    }

    lateinit var originalConsent: Consent
    lateinit var modifiedConsent: Consent
    internal val hiTypes = ArrayList<HiType>()
    internal val linkedAccounts = ArrayList<Links>()

    val fromDateLabel = ObservableField<String>()
    val toDateLabel = ObservableField<String>()
    val expiryDateLabel = ObservableField<String>()
    val expiryTimeLabel = ObservableField<String>()

    val allProvidersChecked = ObservableBoolean(true)
    val saveEnabled = ObservableBoolean(false)

    val onAddChipsEvent = SingleLiveEvent<List<HiType>>()
    val onAddLinksEvent = SingleLiveEvent<List<Links>>()
    val onTimePickerClicked = SingleLiveEvent<String>()
    val onDatePickerClicked = SingleLiveEvent<Pair<Consent, Int>>()
    val onSaveClicked = SingleLiveEvent<Consent>()

    fun setup(originalConsent: Consent) {
        this.originalConsent = originalConsent
        modifiedConsent = this.originalConsent.clone()

        fromDateLabel.set(modifiedConsent.getPermissionStartDate())
        toDateLabel.set(modifiedConsent.getPermissionEndDate())
        expiryDateLabel.set(modifiedConsent.getConsentExpiryDate())
        expiryTimeLabel.set(modifiedConsent.getConsentExpiryTime())
    }

    fun updateHITypesAndLinksReceived(hiTypeAndLinks: HiTypeAndLinks) {
        updateHITypes(hiTypeAndLinks.hiTypes)
        updateProviderLinks(hiTypeAndLinks.linkedAccounts)
    }

    private fun updateHITypes(hiTypes: List<HiType>) {
        this.hiTypes.clear()
        this.hiTypes.addAll(hiTypes)
        onAddChipsEvent.value = hiTypes
    }

    private fun updateProviderLinks(links: List<Links>) {
        linkedAccounts.clear()
        linkedAccounts.addAll(links)
        onAddLinksEvent.value = linkedAccounts
    }

    fun onDateClicked(datePickerId: Int) {
        onDatePickerClicked.value = Pair(modifiedConsent, datePickerId)
    }

    fun onTimePickerClick() {
        onTimePickerClicked.value = modifiedConsent.getConsentExpiryTime()
    }

    fun toggleProvidersSelection() {
        val isChecked = allProvidersChecked.get()
        allProvidersChecked.set(!isChecked)
        linkedAccounts.forEach { link ->
            link.careContexts.forEach { careContext ->
                careContext.contextChecked = allProvidersChecked.get()
            }
        }
        onAddLinksEvent.value = linkedAccounts
        checkEditValid()

    }

    fun onClickSave() {
        if (saveEnabled.get())
            onSaveClicked.value = modifiedConsent
    }

    fun markHITypeChecked(id: String, checked: Boolean) {
        hiTypes.find { it.type == id }?.apply {
            isChecked = checked
        }
        checkEditValid()
    }

    fun checkEditValid() {
        val careContexts = linkedAccounts.flatMap { it.careContexts }
        val selectableItemsCount = careContexts.count()
        val selectionCount = careContexts.count { it.contextChecked }
        allProvidersChecked.set(selectableItemsCount == selectionCount)
        saveEnabled.set(selectionCount > 0)
    }

    fun getConsentRepository(): ConsentRepository = repository

    override fun onDateSelected(datePickerId: Int, date: Date) {
        when (datePickerId) {
            FROM_DATE_DATEPICKER_ID -> {
                modifiedConsent.updateFromDate(date.toUtc())
                fromDateLabel.set(modifiedConsent.getPermissionStartDate())
                checkEditValid()
            }
            TO_DATE_DATEPICKER_ID -> {
                modifiedConsent.updateToDate(date.toUtc())
                toDateLabel.set(modifiedConsent.getPermissionEndDate())
                checkEditValid()
            }
            EXPIRY_DATE_DATEPICKER_ID -> {
                modifiedConsent.permission.dataEraseAt = date.toUtc()
                expiryDateLabel.set(modifiedConsent.getConsentExpiryDate())
                expiryTimeLabel.set(modifiedConsent.getConsentExpiryTime())
                checkEditValid()
            }
        }
    }

    override fun onTimeSelected(timePair: Pair<Int, Int>) {
        checkEditValid()
        with(Calendar.getInstance()) {
            time = DateTimeUtils.getDate(modifiedConsent.permission.dataEraseAt)
            set(Calendar.HOUR_OF_DAY, timePair.first)
            set(Calendar.MINUTE, timePair.second)
            modifiedConsent.permission.dataEraseAt = time.toUtc()
            expiryTimeLabel.set(modifiedConsent.getConsentExpiryTime())
        }
    }
}