package `in`.projecteka.jataayu.user.account.ui.fragment

import `in`.projecteka.jataayu.user.account.databinding.RecoverCmidOtpFrgamentBinding
import `in`.projecteka.jataayu.user.account.viewmodel.RecoverCmidOtpFragmentViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecoverCmidOtpFrgament : Fragment() {

    companion object {
        fun newInstance() = RecoverCmidOtpFrgament()
    }

    private val viewModel: RecoverCmidOtpFragmentViewModel by viewModel()
    private lateinit var binding: RecoverCmidOtpFrgamentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecoverCmidOtpFrgamentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
