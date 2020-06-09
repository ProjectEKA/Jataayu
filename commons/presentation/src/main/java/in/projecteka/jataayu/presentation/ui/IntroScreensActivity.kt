package `in`.projecteka.jataayu.presentation.ui

import `in`.projecteka.jataayu.presentation.IntroScreensActivityViewModel
import `in`.projecteka.jataayu.presentation.R
import `in`.projecteka.jataayu.presentation.databinding.ActivityIntroBinding
import `in`.projecteka.jataayu.util.startLogin
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class IntroScreensActivity : BaseActivity<ActivityIntroBinding>() {

    private var introAdapter: IntroScreensViewpagerAdapter? = null
    lateinit var inflater: LayoutInflater
    private val viewModel: IntroScreensActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBindings()
        initObservers()

        introAdapter = IntroScreensViewpagerAdapter()
        binding.viewPager.adapter = introAdapter
    }

    private fun initBindings() {
        binding.viewModel = viewModel
    }

    private fun initObservers() {
        viewModel.addBottomDotsEvent.observe(this, Observer {
            binding.layoutDots.removeAllViews()
            viewModel.dots = Array<ImageView>(4) {
                var circle = ImageView(this)
                circle.setImageDrawable(getDrawable(viewModel.circleInactive))
                circle.layoutParams = viewModel.layoutParams
                binding.layoutDots?.addView(circle)
                circle
            }

            if (viewModel.dots!!.isNotEmpty()) viewModel.dots!![it]?.setImageDrawable(getDrawable(viewModel.circleActive))
        })

        viewModel.setViewpagerCurrentItemEvent.observe(this, Observer {
            binding.viewPager.currentItem = it
        })

        viewModel.getStartedEvent.observe(this, Observer {
            finish()
            startLogin(this)
        })

        viewModel.init()
    }

    override fun layoutId(): Int = R.layout.activity_intro

    inner class IntroScreensViewpagerAdapter: PagerAdapter()  {

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun getCount(): Int {
            return viewModel.layouts?.size ?: 0
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            var view: View = `object` as View
            container.removeView(view)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
             val view = layoutInflater.inflate(viewModel.layouts!![position], container, false)
            container.addView(view)
            return view
        }
    }
}
