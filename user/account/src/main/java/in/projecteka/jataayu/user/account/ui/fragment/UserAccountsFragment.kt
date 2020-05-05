package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.core.model.HipHiuIdentifiable
import `in`.projecteka.jataayu.core.model.ProviderAddedEvent
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.adapter.ExpandableRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.FragmentUserAccountBinding
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import `in`.projecteka.jataayu.util.startLauncher
import `in`.projecteka.jataayu.util.startProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserAccountsFragment : BaseFragment(), ItemClickCallback {
    private lateinit var binding: FragmentUserAccountBinding
    private val viewModel: UserAccountsViewModel by viewModel()
    private var listItems: List<IDataBindingModel> = emptyList()
    private val eventBusInstance = EventBus.getDefault()

    companion object {
        fun newInstance() = UserAccountsFragment()
        private const val VERIFIED_IDENTIFIER_MOBILE = "MOBILE"
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
        binding.viewModel = viewModel
        initObservers()
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
        viewModel.fetchAll()
    }

    private fun initObservers() {
        viewModel.updateProfile.observe(this, Observer {
            viewModel.preferenceRepository.pinCreated = it.hasTransactionPin
            it.verifiedIdentifiers.forEach { identifier ->
                if (identifier.type == VERIFIED_IDENTIFIER_MOBILE) {
                    viewModel.preferenceRepository.mobileIdentifier = identifier.value
                }
            }
        })
        viewModel.updateLinks.observe(this, Observer {
            listItems = it
            binding.rvUserAccounts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ExpandableRecyclerViewAdapter(this@UserAccountsFragment, this@UserAccountsFragment, it
            )}
        })
        viewModel.userProfileResponse.observe(this, Observer {
            when (it) {
                is Failure -> {
                    context?.showErrorDialog(it.error.localizedMessage)
                }
                is PartialFailure -> {
                    context?.showAlertDialog(
                        getString(R.string.failure), it.error?.message, getString(
                            android
                                .R.string.ok
                        )
                    )
                }
            }
        })
        viewModel.addProviderEvent.observe(this, Observer {
            startProvider(context!!)
        })
        viewModel.logoutResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> {
                    viewModel.showProgress.set(true)
                }
                is Success -> {
                    viewModel.showProgress.set(false)
                    viewModel.clearSharedPreferences()
                    startLauncher(context!!)
                }
                else -> {
                    viewModel.clearSharedPreferences()
                    startLauncher(context!!)
                }
            }
        })

        viewModel.linkedAccountsResponse.observe(viewLifecycleOwner, Observer { links ->
            val hipList = links.map { it.hip }
            getNamesOfHipList(hipList)
        })
    }


    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {
        viewModel.showProgress(true)
    }

    override fun onDestroy() {
        eventBusInstance.unregister(this)
        super.onDestroy()
    }

    @Subscribe
    public fun onEvent(providerAddedEvent: ProviderAddedEvent) {
        when (providerAddedEvent) {
            ProviderAddedEvent.PROVIDER_ADDED -> {
                viewModel.getUserAccounts()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)
    }


    private fun getNamesOfHipList(idList: List<HipHiuIdentifiable>) {
        val hipHiuNameResponse = viewModel.getHipHiuNamesByIdList(idList)
        hipHiuNameResponse.observe(this, Observer { hipHiuNameResponse ->
            if (hipHiuNameResponse.status) {
                val linkedAccountsResponse = viewModel.linkedAccountsResponse.value
                linkedAccountsResponse?.forEach { it.hip.name = hipHiuNameResponse.nameMap[it.hip.getId()] ?: "" }
                viewModel.updateDisplayAccounts(viewModel.linkedAccountsResponse.value)
            } else {
                context?.showErrorDialog(getString(R.string.something_went_wrong))
            }
        })
    }
}


