package `in`.projecteka.jataayu.user.account.ui.fragment


import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.FragmentCreateAccountBinding
import `in`.projecteka.jataayu.user.account.viewmodel.AccountCreationActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.CreateAccountViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateAccountFragment : BaseFragment(), AdapterView.OnItemSelectedListener{

    //private val disposables = CompositeDisposable()
    //    private var isCriteriaMatch: Boolean = true
    //    private var selectedYob: Int? = null
    //    private var ayushmanId: String? = null
    private val viewModel: CreateAccountViewModel by viewModel()
    private val parentVM: AccountCreationActivityViewModel by sharedViewModel()
    private lateinit var binding: FragmentCreateAccountBinding

    companion object {
        fun newInstance() = CreateAccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initBindings()
//        initObservers()
    }
    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, viewModel.getYearsToPopulate())
        binding.spinnerYob.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spinnerYob.setSelection(0)
    }
    private fun initBindings() {
        binding.viewModel = viewModel
        binding.cgGender.setOnCheckedChangeListener(viewModel)
        binding.spinnerListener = this

        binding.etName.addTextChangedListener { text ->
            viewModel.validateName()
        }

        binding.etAyushmanBharatId.addTextChangedListener{text ->
            viewModel.validateAyushmanId()
        }

        binding.btnRegister.setOnClickListener {
            parentVM.redirectToConfirmAccountPage(
                viewModel.inputFullName.get().orEmpty(),
                viewModel.inputAyushmanIdLbl.get().orEmpty(),
                viewModel.selectedYoB,
                viewModel.getGender()
            )
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0) {
            viewModel.selectedYoB((view as AppCompatCheckedTextView).text.toString().toInt())
        }
    }
}
