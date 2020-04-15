package `in`.projecteka.jataayu.registration.ui.fragment

import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.registration.ui.activity.R
import `in`.projecteka.jataayu.registration.ui.activity.databinding.FragmentRegistrationBinding
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationActivityViewModel
import `in`.projecteka.jataayu.registration.viewmodel.RegistrationFragmentViewModel
import `in`.projecteka.jataayu.util.extension.setTitle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment() {
    private lateinit var binding: FragmentRegistrationBinding

    private val parentVM: RegistrationActivityViewModel by sharedViewModel()
    private val viewModel: RegistrationFragmentViewModel by viewModel()

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.onContinueClicked.observe(this, Observer {
            parentVM.requestVerification(it)
        })
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.register)
    }
}