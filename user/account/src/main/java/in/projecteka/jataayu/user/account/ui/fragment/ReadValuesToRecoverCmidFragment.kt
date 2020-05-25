package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.network.utils.Failure
import `in`.projecteka.jataayu.network.utils.Loading
import `in`.projecteka.jataayu.network.utils.PartialFailure
import `in`.projecteka.jataayu.network.utils.Success
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.FragmentReadValuesBinding
import `in`.projecteka.jataayu.user.account.viewmodel.ReadValuesFragmentViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
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
import androidx.lifecycle.Observer
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
            android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, viewModel.getYearsToPopulate()
        )
        binding.spinnerYob.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spinnerYob.setSelection(0)
    }

    private fun initObservers() {

        viewModel.recoverCmidResponse.observe(this, Observer {
            when(it) {
                is Loading -> viewModel.showProgress(it.isLoading)
                is Success -> {
                    parentViewModel.consentManagerId.set(it.data?.cmId)
                    parentViewModel.onDisplayCmidRequest()
                }
                is PartialFailure -> {
                    if (it.error?.code == ReadValuesFragmentViewModel.ERROR_CODE_NO_MATCHING_RECORDS ||
                        it.error?.code == ReadValuesFragmentViewModel.ERROR_CODE_MULTIPLE_MATCHING_RECORDS){
                        parentViewModel.onReviewRequest()
                    } else {
                        activity?.showAlertDialog(
                            getString(R.string.failure), it.error?.message,
                            getString(android.R.string.ok)
                        )
                    }
                }
                is Failure -> {
                    activity?.showErrorDialog(it.error.localizedMessage)
                }
            }
        })
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.spinnerListener = this
        binding.cgGender.setOnCheckedChangeListener(viewModel)

        binding.etName.addTextChangedListener { text: Editable? ->
            viewModel.validateName()
        }

        binding.etAyushmanBharatId.addTextChangedListener{text ->
            viewModel.validateAyushmanId()
        }

        binding.etMobileNumber.addTextChangedListener{ text ->
            viewModel.validateMobileNumber()
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0) {
            viewModel.selectedYoB((view as AppCompatCheckedTextView).text.toString().toInt())
        } else {
            viewModel.selectedYoB(null)
        }
    }

}
