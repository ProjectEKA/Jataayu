package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.databinding.FragmentViewProfileBinding
import `in`.projecteka.jataayu.user.account.ui.activity.EditConsentPinActivity
import `in`.projecteka.jataayu.user.account.viewmodel.ProfileFragmentViewModel
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
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


class ValidateConsentPinFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentViewProfileBinding
    private val viewModel: ProfileFragmentViewModel by viewModel()
//    private val parentViewModel: ResetPasswordActivityViewModel by sharedViewModel()

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

        viewModel.redirectTo.observe(this, Observer {
            if (it == ProfileFragmentViewModel.RedirectTo.CONSENT_PIN) {
                startActivity(Intent(activity, EditConsentPinActivity::class.java))
            }
        })

//        viewModel.isEditMode.observe(this, Observer {
//            if (it) {
//                binding.spinnerListener = this
//            } else {
//                binding.spinnerListener = null
//            }
//            binding.spinnerYob.isEnabled = it

//            binding.spinnerYob.getChildAt(0)?.let {
//                (binding.spinnerYob.getChildAt(0) as TextView).setTextColor(
//                    resources.getColor(R.color.black)
//                )
//            }
//            binding.spinnerYob.alpha = 1.0f;
//            binding.spinnerYob.isClickable = it
//            binding.spinnerYob.isContextClickable = it
//        })
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.spinnerListener = this
//        binding.isEditMode = false
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

}
