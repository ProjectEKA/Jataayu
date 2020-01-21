package `in`.org.projecteka.jataayu.consent.ui.activity

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.FragmentConsentDetailsEditBinding
import `in`.org.projecteka.jataayu.consent.ui.handler.PickerClickHandler
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.presentation.callback.DateTimeSelectionCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.presentation.ui.fragment.DatePickerDialog
import `in`.org.projecteka.jataayu.presentation.ui.fragment.DatePickerDialog.Companion.UNDEFINED_DATE
import `in`.org.projecteka.jataayu.presentation.ui.fragment.TimePickerDialog
import `in`.org.projecteka.jataayu.util.extension.toUtc
import `in`.org.projecteka.jataayu.util.ui.DateTimeUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.google.android.material.chip.Chip
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*


class EditConsentDetailsFragment : BaseFragment(), PickerClickHandler, DateTimeSelectionCallback {
    private lateinit var binding: FragmentConsentDetailsEditBinding

    override fun onTimeSelected(pair: Pair<Int, Int>) {
        val calendar = Calendar.getInstance()
        calendar.time = DateTimeUtils.getDate(modifiedConsent.permission.dataExpiryAt)!!
        calendar.set(Calendar.HOUR_OF_DAY, pair.first)
        calendar.set(Calendar.MINUTE, pair.second)
        modifiedConsent.permission.dataExpiryAt = calendar.time.toUtc()
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
        TimePickerDialog(modifiedConsent.getConsentExpiryTime(), this).show(fragmentManager!!, modifiedConsent.getConsentExpiryTime())
    }

    override fun onDatePickerClick(view: View) {
        when (view.id) {
            R.id.tv_requests_info_from -> {
                val from = DateTimeUtils.getDate(modifiedConsent.permission.dateRange.from)?.time!!
                DatePickerDialog(R.id.tv_requests_info_from, from, UNDEFINED_DATE, System.currentTimeMillis(), this).show(
                    fragmentManager!!,
                    modifiedConsent.permission.dateRange.from
                )
            }
            R.id.tv_requests_info_to -> {
                val to = DateTimeUtils.getDate(modifiedConsent.permission.dateRange.to)?.time!!
                DatePickerDialog(R.id.tv_requests_info_to, to, UNDEFINED_DATE, System.currentTimeMillis(), this).show(
                    fragmentManager!!,
                    modifiedConsent.permission.dateRange.to
                )
            }
            R.id.tv_expiry_date -> {
                DatePickerDialog(R.id.tv_expiry_date, DateTimeUtils.getDate(modifiedConsent.permission.dataExpiryAt)?.time!!, this).show(
                    fragmentManager!!,
                    modifiedConsent.permission.dataExpiryAt
                )
            }
        }
    }

    private val eventBusInstance: EventBus = EventBus.getDefault()
    lateinit var consent: Consent
    lateinit var modifiedConsent: Consent

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
        modifiedConsent = consent
        binding.consent = modifiedConsent

        for (hiType in modifiedConsent.hiTypes) {
            binding.cgRequestInfoTypes.addView(newChip(hiType.description))
        }

        binding.pickerClickHandler = this
    }

    private fun newChip(description: String): Chip? {
        val chip = Chip(context)
        chip.text = description
        return chip
    }

    @Subscribe
    public fun onConsentReceived(consent: Consent) {
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
}
