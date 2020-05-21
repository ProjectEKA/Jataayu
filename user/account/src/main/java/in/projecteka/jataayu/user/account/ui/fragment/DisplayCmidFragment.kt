package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.FragmentDisplayCmidBinding
import `in`.projecteka.jataayu.user.account.viewmodel.DisplayCmidFragmentViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class DisplayCmidFragment : Fragment()  {

    private lateinit var binding: FragmentDisplayCmidBinding
    private val viewModel: DisplayCmidFragmentViewModel by viewModel()
    private val parentViewModel: RecoverCmidActivityViewModel by sharedViewModel()

    companion object {
        fun newInstance() = DisplayCmidFragment ()
        private const val SPACE = " "
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisplayCmidBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initObservers()
        parentViewModel.consentManagerId.get()?.let {

            val spannableTitle = SpannableStringBuilder()
                .bold { append(parentViewModel.consentManagerId.get()) }
                .append(SPACE + getString(R.string.display_cmid_title))

            viewModel.init(spannableTitle)
        }
    }

    private fun initObservers() {
        viewModel.redirectToEvent.observe(this, Observer {
            if (it == DisplayCmidFragmentViewModel.Redirect.REVIEW) {
               fragmentManager?.popBackStack()
            } else if (it == DisplayCmidFragmentViewModel.Redirect.BACK_TO_LOGIN){
                activity!!.finish()
            }
        })
    }

    private fun initBindings() {
        binding.viewModel = viewModel
    }
}
