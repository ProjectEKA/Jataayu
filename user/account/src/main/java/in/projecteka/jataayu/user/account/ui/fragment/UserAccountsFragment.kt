package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.adapter.ExpandableRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.IGroupDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.FragmentUserAccountBinding
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import `in`.projecteka.jataayu.util.extension.get
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.sharedPref.setMobileIdentifier
import `in`.projecteka.jataayu.util.sharedPref.setName
import `in`.projecteka.jataayu.util.sharedPref.setPinCreated
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserAccountsFragment : BaseFragment(), ItemClickCallback, ResponseCallback {
    private lateinit var binding: FragmentUserAccountBinding
    private val viewModel: UserAccountsViewModel by viewModel()
    private var listItems: List<IDataBindingModel> = emptyList()
    private var compositeDisposable = CompositeDisposable()
    private val eventBusInstance = EventBus.getDefault()

    private val observer = Observer<LinkedAccountsResponse> {
        binding.linkedPatient = it.linkedPatient
        val hipList = it.linkedPatient.links.map { links -> links.hip }
        getNamesOfHiuList(hipList)
    }

    private val profileObserver = Observer<MyProfile> {
        viewModel.preferenceRepository.pinCreated = it.hasTransactionPin
        it.verifiedIdentifiers.forEach { identifier ->
            if (identifier.type == VERIFIED_IDENTIFIER_MOBILE) {
                viewModel.preferenceRepository.mobileIdentifier = identifier.value
            }
        }
        viewModel.preferenceRepository.name = it.name
        binding.tvPatientName.text = it.name
    }

    companion object {
        fun newInstance() = UserAccountsFragment()
        private val VERIFIED_IDENTIFIER_MOBILE = "MOBILE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderUi()
    }

    private fun renderUi() {
        initObservers()
        showProgressBar(true)
        viewModel.getUserAccounts(this)
        viewModel.getMyProfile(this)
    }

    private fun initObservers() {
        viewModel.linkedAccountsResponse.observe(this, observer)
        viewModel.myProfileResponse.observe(this, profileObserver)
    }

    private fun getUserAccounts(hipHiuNameResponse: HipHiuNameResponse) {
        compositeDisposable.add(Observable.just(viewModel)
            .map { it.getDisplayAccounts(hipHiuNameResponse) }
            .get()
            .subscribe { items ->
                listItems = items
                binding.rvUserAccounts.apply {
                    layoutManager = LinearLayoutManager(context)
                    @Suppress("UNCHECKED_CAST")
                    (listItems as? List<IGroupDataBindingModel>)?.let {
                        adapter = ExpandableRecyclerViewAdapter(
                            this@UserAccountsFragment,
                            this@UserAccountsFragment,
                            it
                        )
                    }
                }
            })
    }

    private fun getNamesOfHiuList(hiuList: List<HipHiuIdentifiable>) {
        val hipHiuNameResponse = viewModel.fetchHipHiuNamesOfHiuList(hiuList)
        hipHiuNameResponse.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                getUserAccounts(hipHiuNameResponse = it)
            } else {
                context?.showLongToast(getString(R.string.something_went_wrong))
            }
        })
    }


    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        showProgressBar(false)
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        showProgressBar(false)
        context?.showAlertDialog(
            getString(R.string.failure),
            errorBody.error.message,
            getString(android.R.string.ok)
        )
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        context?.showErrorDialog(t.localizedMessage)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        eventBusInstance.unregister(this)
        super.onDestroy()
    }

    @Subscribe
    public fun onEvent(providerAddedEvent: ProviderAddedEvent) {
        when (providerAddedEvent) {
            ProviderAddedEvent.PROVIDER_ADDED -> {
                showProgressBar(true)
                viewModel.getUserAccounts(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)
    }
}


