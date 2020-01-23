package `in`.org.projecteka.jataayu.consent.ui.fragment


import DividerItemDecorator
import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.databinding.FragmentConfirmConsentBinding
import `in`.org.projecteka.jataayu.consent.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.consent.model.Links
import `in`.org.projecteka.jataayu.consent.ui.handler.ConfirmConsentHandler
import `in`.org.projecteka.jataayu.consent.ui.handler.ProviderSelectionClickHandler
import `in`.org.projecteka.jataayu.consent.viewmodel.ConfirmConsentViewModel
import `in`.org.projecteka.jataayu.core.databinding.PatientAccountResultItemBinding
import `in`.org.projecteka.jataayu.core.model.CareContext
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.util.extension.setTitle
import `in`.org.projecteka.jataayu.util.extension.showLongToast
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_confirm_consent.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConfirmConsentFragment : BaseFragment(), ItemClickCallback, ProviderSelectionClickHandler,
    ConfirmConsentHandler, ProgressDialogCallback {
    lateinit var binding: FragmentConfirmConsentBinding

    lateinit var listItems: List<IDataBindingModel>

    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        val checkbox = (itemViewBinding as PatientAccountResultItemBinding).cbCareContext
        checkbox.toggle()
        if (!checkbox.isChecked) cb_link_all_providers.isChecked = false
    }
    private val eventBusInstance = EventBus.getDefault()

    private val viewModel: ConfirmConsentViewModel by sharedViewModel()

    private val linkedAccountsObserver = Observer<LinkedAccountsResponse> {
        renderLinkedAccounts(it.links)
    }

    private fun renderLinkedAccounts(linkedAccounts: List<Links>) {
        listItems = getItems(linkedAccounts)

        rvLinkedAccounts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(this@ConfirmConsentFragment, listItems)
            addItemDecoration(
                DividerItemDecorator(
                    ContextCompat.getDrawable(
                        context!!,
                        R.color.transparent
                    )!!
                )
            )
        }

    }

    private fun getItems(linkedAccounts: List<Links>): List<IDataBindingModel> {
        val items = arrayListOf<IDataBindingModel>()
        for (linkedAccount in linkedAccounts) {
            items.add(linkedAccount.hip)
            for (careContext in linkedAccount.careContexts) {
                items.add(careContext)
            }
        }
        return items
    }

    companion object {

        fun newInstance() = ConfirmConsentFragment()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmConsentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        renderUi()
    }

    private fun renderUi() {
        val consent = eventBusInstance.getStickyEvent(Consent::class.java)
        setRequesterName(consent.requester.name)
        showProgressBar(true)
        viewModel.linkedAccountsResponse.observe(this, linkedAccountsObserver)
        if (viewModel.linkedAccountsResponse.value == null) {
            viewModel.getLinkedAccounts(consent.id, this)
        }
    }

    private fun setRequesterName(name: String) {
        val label = String.format(resources.getString(R.string.confirm_consent_label), name)
        val spannableStringBuilder = SpannableStringBuilder(label)
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            label.indexOf(name),
            label.indexOf(name) + name.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        lbl_title.text = spannableStringBuilder
    }

    private fun initBindings() {
        binding.clickHandler = this
        binding.confirmClickHandler = this
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


    override fun toggleProvidersSelection(view: View) {
        val checked = (view as CheckBox).isChecked
        for (listItem in listItems) {
            if (listItem is CareContext) {
                listItem.contextChecked = checked
            }
        }
        rvLinkedAccounts.adapter?.notifyDataSetChanged()
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.confirm_request)
    }

    override fun confirmConsent(view: View) {
        showLongToast("Confirm consent")
    }

    override fun onSuccess(any: Any?) {
        showProgressBar(false)
    }

    override fun onFailure(any: Any?) {
        showProgressBar(false)
    }

    private fun showProgressBar(shouldShow : Boolean) {
        binding.progressBarVisibility = shouldShow
    }
}
