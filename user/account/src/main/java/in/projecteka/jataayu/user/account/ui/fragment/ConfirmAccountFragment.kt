package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.user.account.databinding.ConfirmAccountFragmentBinding
import `in`.projecteka.jataayu.user.account.viewmodel.AccountCreationActivityViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.ConfirmAccountViewModel
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class ConfirmAccountFragment : BaseFragment() {

    private lateinit var binding: ConfirmAccountFragmentBinding

    companion object {
        fun newInstance() = ConfirmAccountFragment()
    }

    private val viewModel: ConfirmAccountViewModel by viewModel()
    private val parentVM : AccountCreationActivityViewModel by sharedViewModel()


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmAccountFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        val generatedCmID = generateCmId(removeCountryCode(viewModel.getMobileIdentifier()), parentVM.fullName)
        viewModel.inputUsernameLbl.set(generatedCmID)
    }

    private fun initBindings() {
        binding.viewModel = viewModel
        binding.etPassword.addTextChangedListener{text: Editable? ->
            viewModel.validatePassword()
        }
        binding.etConfirmPassword.addTextChangedListener{text: Editable? ->
            viewModel.validateConfirmPassword()
        }

        binding.etUsername.addTextChangedListener{text: Editable? ->
            viewModel.validateConfirmPassword()
        }

        viewModel.inputUsernameLbl.set(parentVM.fullName)
        viewModel.inputFullName .set(parentVM.fullName)
        viewModel.inputGender.set(parentVM.gender)
        viewModel.selectedYoB.set(parentVM.yearOfBirth)
    }


    private fun generateCheckDigit(digits: CharSequence): Char {
        val lastIndex = digits.lastIndex
        val lastIndexRem = lastIndex.rem(2)
        val n = 10
        val luhnSum = digits.foldRightIndexed(0, { index, c, acc ->
            val currentRem = index.rem(2)
            val numericValue = Character.getNumericValue(c)
            var addend = if (lastIndexRem == currentRem) 2 * numericValue else numericValue
            addend = addend.div(n) + addend.rem(n)
            acc + addend
        })
        val checkDigit = (n - luhnSum.rem(n)) % n
        return checkDigit.toString()[0]
    }

    private fun cmIdToDigitSeq(cmId: String): String {
        return cmId.fold("", { result, ch ->
            if (ch == '.') result else result + ch.toInt()
        })
    }

    private fun generateCmId(mobile: String, name: String): String {
        val rawCmId = mobile + "." + name.toUpperCase(Locale.ROOT).take(3)
        val digitSeq = cmIdToDigitSeq(rawCmId)
        val firstCheckDigit = generateCheckDigit(digitSeq)
        val secondCheckDigit = generateCheckDigit(digitSeq + firstCheckDigit)
        return "$rawCmId.$firstCheckDigit$secondCheckDigit"
    }

    private fun removeCountryCode(mobileNumber: String) : String{
        return mobileNumber.split("-")[1]
    }
}
