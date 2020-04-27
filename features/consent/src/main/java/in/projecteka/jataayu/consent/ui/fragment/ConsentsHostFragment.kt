package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.databinding.FragmentConsentHostBinding
import `in`.projecteka.jataayu.consent.ui.adapter.ConsentPagerAdapter
import `in`.projecteka.jataayu.consent.viewmodel.ConsentHostFragmentViewModel
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_consent_host.*
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConsentHostFragment : BaseFragment() {

    private lateinit var binding: FragmentConsentHostBinding

    private val eventBusInstance = EventBus.getDefault()

    private val viewModel: ConsentHostFragmentViewModel by sharedViewModel()

    companion object {
        fun newInstance() = ConsentHostFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsentHostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        initialViewpagerSetup()
        initObservers()
    }

    private fun initialViewpagerSetup() {
        binding.viewPager.adapter = ConsentPagerAdapter(context!!, childFragmentManager)
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private fun initObservers() {
        viewModel.redirectEvent.observe(this, Observer {
            when (it) {
                ConsentHostFragmentViewModel.Action.SELECT_CONSENTS_TAB -> {
                    activity?.runOnUiThread {
                        binding.viewPager.currentItem = 1
                    }
                }
            }
        })
        viewModel.showToastEvent.observe(this, Observer {
            showSnackbar(it)
        })
    }

    private fun showSnackbar(message: String) {
        val spannableString = SpannableString(message)
        spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val snackbar = Snackbar.make(host_container, spannableString, 2000)
        snackbar.show()
    }
}


