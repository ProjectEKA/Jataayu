package `in`.org.projecteka.jataayu.user.account.ui.fragment

import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.user.account.databinding.FragmentUserAccountBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding


class UserAccountsFragment : BaseFragment(), ItemClickCallback {
    override fun onItemClick(
        iDataBindingModel: IDataBindingModel,
        itemViewBinding: ViewDataBinding
    ) {

    }

    private lateinit var binding: FragmentUserAccountBinding

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
    }
}


