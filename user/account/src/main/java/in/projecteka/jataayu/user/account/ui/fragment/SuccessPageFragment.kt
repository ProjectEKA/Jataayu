package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.databinding.SuccessPageFragmentBinding
import `in`.projecteka.jataayu.user.account.viewmodel.ConfirmAccountViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.SuccessPageViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuccessPageFragment : Fragment() {

    private lateinit var binding: SuccessPageFragmentBinding

    companion object {
        fun newInstance() = SuccessPageFragment()
    }

    private val viewModel: SuccessPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBindings()
        binding = SuccessPageFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
    }

    fun initBindings(){
        binding.viewModel = viewModel
    }

}
