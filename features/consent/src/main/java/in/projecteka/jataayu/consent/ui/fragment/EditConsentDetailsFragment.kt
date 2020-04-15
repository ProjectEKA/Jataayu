package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.R
import `in`.projecteka.jataayu.consent.databinding.FragmentConsentDetailsEditBinding
import `in`.projecteka.jataayu.consent.viewmodel.EditConsentDetailsVM
import `in`.projecteka.jataayu.core.databinding.PatientAccountResultItemBinding
import `in`.projecteka.jataayu.core.model.CareContext
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.decorator.DividerItemDecorator
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.presentation.ui.fragment.DatePickerDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.DatePickerDialog.Companion.UNDEFINED_DATE
import `in`.projecteka.jataayu.presentation.ui.fragment.TimePickerDialog
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_consent_details_edit.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditConsentDetailsFragment : BaseFragment(), ItemClickCallback, CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: FragmentConsentDetailsEditBinding

    private val viewModel: EditConsentDetailsVM by viewModel()
    private val eventBusInstance: EventBus = EventBus.getDefault()

    private lateinit var listItems: List<IDataBindingModel>
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
        viewModel.setup(eventBusInstance.getStickyEvent(Consent::class.java))
        binding.viewModel = viewModel
        initObservers()
    }

    private fun initObservers() {

        viewModel.onAddLinksEvent.observe(this, androidx.lifecycle.Observer { links ->
            val linkDataModels = arrayListOf<IDataBindingModel>()
            links.forEach { link ->
                linkDataModels.add(link.hip)
                linkDataModels.addAll(link.careContexts)
            }
            listItems = linkDataModels
            genericRecyclerViewAdapter = GenericRecyclerViewAdapter(listItems, this)
            rvLinkedAccounts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = genericRecyclerViewAdapter
                val dividerItemDecorator = DividerItemDecorator(ContextCompat.getDrawable(context!!, R.color.transparent)!!)
                addItemDecoration(dividerItemDecorator)
            }
        })

        viewModel.onAddChipsEvent.observe(this, androidx.lifecycle.Observer { HITypes ->
            HITypes?.forEach {
                binding.cgRequestInfoTypes.addView(newChip(it.type, it.isChecked))
            }
        })

        viewModel.onTimePickerClicked.observe(this, androidx.lifecycle.Observer {
            TimePickerDialog(it, viewModel).show(fragmentManager!!, it)
        })

        viewModel.onDatePickerClicked.observe(this, androidx.lifecycle.Observer { consentAndIdPair ->
            val consentPermission = consentAndIdPair.first.permission
            when (consentAndIdPair.second) {
                EditConsentDetailsVM.FROM_DATE_DATEPICKER_ID -> {
                    val from = DateTimeUtils.getDate(consentPermission.dateRange.from)?.time ?: 0
                    DatePickerDialog(
                        consentAndIdPair.second,
                        from, UNDEFINED_DATE,
                        System.currentTimeMillis(), viewModel
                    )
                        .show(fragmentManager!!, consentPermission.dateRange.from)
                }
                EditConsentDetailsVM.TO_DATE_DATEPICKER_ID -> {
                    val to = DateTimeUtils.getDate(consentPermission.dateRange.to)?.time ?: 0
                    val from = DateTimeUtils.getDate(consentPermission.dateRange.from)?.time ?: 0
                    DatePickerDialog(
                        consentAndIdPair.second,
                        to, from, System.currentTimeMillis(), viewModel
                    )
                        .show(fragmentManager!!, consentPermission.dateRange.to)
                }
                EditConsentDetailsVM.EXPIRY_DATE_DATEPICKER_ID -> {
                    DatePickerDialog(
                        consentAndIdPair.second,
                        DateTimeUtils.getDate(consentPermission.dataEraseAt)?.time ?: 0,
                        System.currentTimeMillis(), UNDEFINED_DATE, viewModel
                    )
                        .show(fragmentManager!!, consentPermission.dataEraseAt)
                }
            }
        })

        viewModel.onSaveClicked.observe(this, Observer {

            eventBusInstance.postSticky(it)
            activity?.onBackPressed()
        })

    }

    private fun newChip(description: String, isChecked: Boolean) = Chip(context).apply {
        text = description
        this.isChecked = isChecked
        setOnCheckedChangeListener(this@EditConsentDetailsFragment)
    }


    @Subscribe(sticky = true)
    public fun onHitypesAndLinkesReceived(hiTypeAndLinks: HiTypeAndLinks) {
        viewModel.updateHITypesAndLinksReceived(hiTypeAndLinks)
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

    override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
        if (itemViewBinding is PatientAccountResultItemBinding) { //Check if it header or item
            val checkbox = itemViewBinding.cbCareContext
            checkbox.toggle()
            (iDataBindingModel as CareContext).contextChecked = checkbox.isChecked

            viewModel.checkEditValid()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        viewModel.markHITypeChecked(buttonView?.text.toString(),isChecked)
    }
}
