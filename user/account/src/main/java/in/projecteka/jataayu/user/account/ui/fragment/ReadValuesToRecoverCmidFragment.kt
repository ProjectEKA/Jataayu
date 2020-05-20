package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.databinding.FragmentReadValuesBinding
import `in`.projecteka.jataayu.user.account.viewmodel.ReadValuesFragmentViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
import android.R
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadValuesToRecoverCmidFragment : Fragment(), AdapterView.OnItemSelectedListener  {

    private lateinit var binding: FragmentReadValuesBinding
    private val viewModel: ReadValuesFragmentViewModel by viewModel()
    private val parentViewModel: RecoverCmidActivityViewModel by sharedViewModel()

    companion object {
        fun newInstance() = ReadValuesToRecoverCmidFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadValuesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initObservers()
        initSpinner()
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter<String>(
            activity!!,
            R.layout.simple_spinner_dropdown_item, R.id.text1, viewModel.getYearsToPopulate()
        )
        binding.spinnerYob.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spinnerYob.setSelection(0)
    }

    private fun initObservers() {
//        viewModel.generateOtpResponse.observe(this, Observer {
//            when(it){
//                is Loading -> viewModel.showProgress(it.isLoading)
//                is Success -> {
////                    parentViewModel.addedConsentManagerId.set(it.data?.otpMediumValue)
//                    parentViewModel.sessionId = it.data?.sessionId
//                    parentViewModel.onOtpFragmentRedirectRequest()
//                }
//                is PartialFailure -> {
//                    activity?.showAlertDialog(
//                        getString(R.string.failure), it.error?.message,
//                        getString(android.R.string.ok)
//                    )
//                }
//                is Failure -> {
//                    activity?.showErrorDialog(it.error.localizedMessage)
//                }
//            }
//        })
//        viewModel.consentManagerIdField.observe(this, Observer {
//            parentViewModel.consentManagerId = it
//        })
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.spinnerListener = this

        binding.etName.addTextChangedListener { text: Editable? ->
            viewModel.validateName()
        }

        binding.etAyushmanBharatId.addTextChangedListener{text ->
            viewModel.validateAyushmanId()
        }

        binding.etMobileNumber.addTextChangedListener{ text ->
            viewModel.validateMobileNumber()
            }
//        binding.etConsentManagerId.addTextChangedListener { text: Editable? ->
//            viewModel.validateConsentManagerId(viewModel.inputConsentManagerId.get().toString())
//        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0) {
            viewModel.selectedYoB((view as AppCompatCheckedTextView).text.toString().toInt())
        }
    }

}
