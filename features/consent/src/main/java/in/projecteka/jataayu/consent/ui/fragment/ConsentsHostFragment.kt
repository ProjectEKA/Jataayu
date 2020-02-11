package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.consent.databinding.FragmentConsentHostBinding
import `in`.projecteka.jataayu.consent.ui.adapter.ConsentPagerAdapter
import `in`.projecteka.jataayu.core.model.MessageEventType
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_consent.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ConsentHostFragment : BaseFragment() {

    private lateinit var binding: FragmentConsentHostBinding

    private val eventBusInstance = EventBus.getDefault()

    companion object {
        fun newInstance() = ConsentHostFragment()
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onTabPositionReceived(messageEventType: MessageEventType) {
        if (messageEventType == MessageEventType.SELECT_CONSENTS_TAB) {
            activity?.runOnUiThread {
                binding.viewPager.currentItem = 1
            }
        }
        eventBusInstance.removeStickyEvent(MessageEventType::class.java)
    }

    override fun onStart() {
        super.onStart()
        if (!eventBusInstance.isRegistered(this))
            eventBusInstance.register(this)
    }

    override fun onDestroy() {
        eventBusInstance.unregister(this)
        super.onDestroy()
    }
}


