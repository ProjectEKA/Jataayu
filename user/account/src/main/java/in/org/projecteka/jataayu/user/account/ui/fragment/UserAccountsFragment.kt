package `in`.org.projecteka.jataayu.user.account.ui.fragment

import `in`.org.projecteka.jataayu.core.model.LinkedAccountsResponse
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.callback.ProgressDialogCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.user.account.databinding.FragmentUserAccountBinding
import `in`.org.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserAccountsFragment : BaseFragment(), ItemClickCallback, ProgressDialogCallback {
    private lateinit var binding: FragmentUserAccountBinding
    private val viewModel : UserAccountsViewModel by viewModel()
    private lateinit var listItems: List<IDataBindingModel>

    private val observer = Observer<LinkedAccountsResponse> {
        binding.linkedPatient = it.linkedPatient
        getUserAccounts()
    }

    companion object {
        fun newInstance() = UserAccountsFragment()
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
        viewModel.linkedAccountsResponse.observe(this, observer)
        showProgressBar(true)
        viewModel.getUserAccounts(this)
    }

    private fun getUserAccounts() {
        listItems = viewModel.getDisplayAccounts()
        binding.rvUserAccounts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericRecyclerViewAdapter(this@UserAccountsFragment, listItems)
        }

    }

    override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
        showProgressBar(false)
    }

    override fun onSuccess(any: Any?) {
        showProgressBar(false)
    }

    override fun onFailure(any: Any?) {
        showProgressBar(false)
    }
}


