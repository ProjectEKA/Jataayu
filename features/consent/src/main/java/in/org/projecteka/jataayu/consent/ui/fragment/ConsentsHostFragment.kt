package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.consent.databinding.FragmentConsentHostBinding
import `in`.org.projecteka.jataayu.consent.ui.adapter.ConsentPagerAdapter
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_consent.*

class ConsentHostFragment : BaseFragment() {

    private lateinit var binding: FragmentConsentHostBinding

    companion object {
        fun newInstance() = ConsentHostFragment()
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            view_pager.adapter?.notifyDataSetChanged()
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

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
        val viewPager = binding.viewPager
        viewPager.adapter = ConsentPagerAdapter(context!!, activity!!.supportFragmentManager)
        val tabs = binding.tabs
        tabs.setupWithViewPager(viewPager)

        view_pager.addOnPageChangeListener(onPageChangeListener)
    }
}


