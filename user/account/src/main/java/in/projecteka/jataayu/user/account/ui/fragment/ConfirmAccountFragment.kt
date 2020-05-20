package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.ConfirmAccountFragmentBinding
import `in`.projecteka.jataayu.user.account.viewmodel.ConfirmAccountViewModel
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel


class ConfirmAccountFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmAccountFragment()
    }

    private val viewModel: ConfirmAccountViewModel by viewModel()


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confirm_account_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showProgress(false)
    }

//    private fun getPasswordInputType(): Int {
//        return InputType.TYPE_CLASS_TEXT + TYPE_TEXT_VARIATION_PASSWORD
//    }

}
