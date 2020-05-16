package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.registration.ui.activity.databinding.LoginOtpFragmentBinding
import `in`.projecteka.jataayu.registration.viewmodel.LoginOtpViewModel
import `in`.projecteka.jataayu.registration.viewmodel.LoginViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginOtpFragment : Fragment() {

    companion object {
        fun newInstance() = LoginOtpFragment()
    }

    private val viewModel: LoginOtpViewModel by viewModel()
    private val loginViewModel: LoginViewModel by sharedViewModel()
    private lateinit var binding: LoginOtpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginOtpFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        return binding.root
    }

}
