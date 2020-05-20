package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ConfirmAccountFragmentBinding
import `in`.projecteka.jataayu.user.account.viewmodel.ConfirmAccountViewModel
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel


class ConfirmAccountFragment : BaseFragment() {

    private lateinit var binding: ConfirmAccountFragmentBinding

    companion object {
        fun newInstance() = ConfirmAccountFragment()
    }

    private val viewModel: ConfirmAccountViewModel by viewModel()


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmAccountFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBindings()
    }

    private fun initBindings() {
        binding.viewModel = viewModel
    }
}
