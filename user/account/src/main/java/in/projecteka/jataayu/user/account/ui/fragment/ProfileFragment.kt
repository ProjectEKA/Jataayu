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
import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

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
        initSpinner()
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

        viewModel.redirectTo.observe(this, Observer {
            if (it == ProfileFragmentViewModel.RedirectTo.CHANGE_PASSWORD) {
                startChangePassword(activity!!)
            }
        })
        viewModel.redirectTo.observe(this, Observer {
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
        binding.spinnerListener = this
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter<String>(
            activity!!,
            R.layout.simple_spinner_dropdown_item, R.id.text1, viewModel.getYearsToPopulate()
        )

        binding.spinnerYob.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spinnerYob.setSelection(arrayAdapter.getPosition(viewModel.preferenceRepository.yearOfBirth.toString()))
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        view?.let {
            if (position != 0) {
                viewModel.selectedYoB((view as AppCompatCheckedTextView).text.toString().toInt())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setConsentPinStatus()
    }

}
