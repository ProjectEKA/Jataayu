package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.core.ConsentScopeType
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.user.account.databinding.FragmentViewProfileBinding
import `in`.projecteka.jataayu.user.account.viewmodel.ProfileFragmentViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.ProfileFragmentViewModel.Companion.KEY_SCOPE_TYPE
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import `in`.projecteka.jataayu.util.startChangePassword
import `in`.projecteka.jataayu.util.startCreatePin
import `in`.projecteka.jataayu.util.startLauncher
import `in`.projecteka.jataayu.util.startPinVerification
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(){

    private lateinit var binding: FragmentViewProfileBinding
    private val viewModel: ProfileFragmentViewModel by viewModel()

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initObservers()
        viewModel.init()
        initGender()
    }

    private fun initGender() {
        when (viewModel.preferenceRepository.gender) {
            PreferenceRepository.GENDER_MALE -> {
                binding.cgGender.check(binding.cgGender.getChildAt(0).id)
            }
            PreferenceRepository.GENDER_FEMALE -> {
                binding.cgGender.check(binding.cgGender.getChildAt(1).id)
            }
            else -> {
                binding.cgGender.check(binding.cgGender.getChildAt(2).id)
            }
        }
    }

    private fun initObservers() {

        viewModel.logoutResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Loading -> {
                    viewModel.showProgress.set(true)
                }
                else -> {
                    onLogoutFinish()
                }
            }
        })

        viewModel.redirectTo.observe(viewLifecycleOwner, Observer {
            if (it == ProfileFragmentViewModel.RedirectTo.CONSENT_PIN) {
                if (viewModel.preferenceRepository.pinCreated){
                    startPinVerification(activity!!) {
                        putExtra(KEY_SCOPE_TYPE, ConsentScopeType.SCOPE_PIN_VERIFY.ordinal)
                    }
                } else {
                    startCreatePin(activity!!) {
                        putExtra(KEY_SCOPE_TYPE, ConsentScopeType.SCOPE_PIN_VERIFY.ordinal)
                    }
                }
            } else if (it == ProfileFragmentViewModel.RedirectTo.CHANGE_PASSWORD) {
                startChangePassword(activity!!)
            }
        })
    }

    private fun onLogoutFinish() {
        viewModel.showProgress.set(false)
        viewModel.clearSharedPreferences()
        activity?.finish()
        startLauncher(context!!) {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    private fun initBindings() {
        binding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.setConsentPinStatus()
    }

}
