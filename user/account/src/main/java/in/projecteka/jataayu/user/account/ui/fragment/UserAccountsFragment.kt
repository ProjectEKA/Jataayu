package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.core.model.ProviderAddedEvent
import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.presentation.adapter.ExpandableRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.FragmentUserAccountBinding
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initObservers()
        renderUi()
    }

    private fun renderUi() {
        viewModel.fetchAll()
    }

    private fun initObservers() {
        viewModel.updateProfile.observe(this, Observer {
            context?.setPinCreated(it.hasTransactionPin)
            it.verifiedIdentifiers.forEach { identifier ->
                if (identifier.type == VERIFIED_IDENTIFIER_MOBILE) {
                    activity?.setMobileIdentifier(identifier.value)
                }
            }
            context?.setName(it.name)
        })
        viewModel.updateLinks.observe(this, Observer {
            listItems = it
            binding.rvUserAccounts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ExpandableRecyclerViewAdapter(this@UserAccountsFragment, this@UserAccountsFragment, it)
            }
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
//                showProgressBar(true)
                viewModel.getUserAccounts()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)
    }
}


