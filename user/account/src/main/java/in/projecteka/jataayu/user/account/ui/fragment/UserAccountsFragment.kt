package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.projecteka.jataayu.core.model.MyProfile
import `in`.projecteka.jataayu.core.model.ProviderAddedEvent
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.network.utils.Success
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
import `in`.projecteka.jataayu.util.startLauncher
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
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
        getUserAccounts()
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
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            setHasOptionsMenu(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderUi()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> false
            R.id.action_logout -> {
                viewModel.logout()
                return false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun renderUi() {
        initObservers()
        showProgressBar(true)
        viewModel.getUserAccounts(this)
        viewModel.getMyProfile(this)
        viewModel.logoutResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> {
                    showProgressBar(true)
                }
                is Success -> {
                    showProgressBar(false)
                    viewModel.clearSharedPreferences()
                    startLauncher(context!!)
                }
                else -> {
                    viewModel.clearSharedPreferences()
                    startLauncher(context!!)
                }
            }
        })
    }

    private fun initObservers() {
        viewModel.linkedAccountsResponse.observe(this, observer)
        viewModel.myProfileResponse.observe(this, profileObserver)
    }

    private fun getUserAccounts() {
        compositeDisposable.add(Observable.just(viewModel)
            .map { it.getDisplayAccounts() }
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


