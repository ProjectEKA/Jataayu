package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.registration.ui.activity.databinding.PasswordInputFragmentBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import `in`.projecteka.jataayu.registration.viewmodel.PasswordInputViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordInputFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordInputFragment()
    }

    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val viewModel: PasswordInputViewModel by viewModel()

    private lateinit var binding: PasswordInputFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PasswordInputFragmentBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        initObserver()
        return binding.root
    }

    private fun initObserver() {

        viewModel.onPasswordVisibilityToggleEvent.observe(viewLifecycleOwner, Observer {
            binding.etPassword.setSelection(it)
        })

    }

}
