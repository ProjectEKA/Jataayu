package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.databinding.FragmentNoMatchingRecordsBinding
import `in`.projecteka.jataayu.user.account.viewmodel.NoMatchingRecordsFragmentViewModel
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidActivityViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NoMatchingRecordsFragment : Fragment()  {

    private lateinit var binding: FragmentNoMatchingRecordsBinding
    private val viewModel: NoMatchingRecordsFragmentViewModel by viewModel()
    private val parentViewModel: RecoverCmidActivityViewModel by sharedViewModel()

    companion object {
        fun newInstance() = NoMatchingRecordsFragment ()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoMatchingRecordsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initObservers()
    }

    private fun initObservers() {
        viewModel.redirectToReviewEvent.observe(this, Observer {
            fragmentManager?.popBackStack()
        })
    }

    private fun initBindings() {
        binding.viewModel = viewModel
    }
}
