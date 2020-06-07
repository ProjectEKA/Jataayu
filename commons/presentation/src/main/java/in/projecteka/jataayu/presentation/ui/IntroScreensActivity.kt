package `in`.projecteka.jataayu.presentation.ui

import `in`.projecteka.jataayu.presentation.R
import `in`.projecteka.jataayu.presentation.databinding.ActivityIntroBinding
import `in`.projecteka.jataayu.util.extension.showShortToast
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class IntroScreensActivity : BaseActivity<ActivityIntroBinding>(), OnPageChangeListener {

    private lateinit var dots: Array<ImageView>
    private var layouts: Array<Int>? = null
    private var introAdapter: IntroScreensViewpagerAdapter? = null
    lateinit var inflater: LayoutInflater


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layouts = arrayOf(R.layout.intro_screen1, R.layout.intro_screen2, R.layout.intro_screen3, R.layout.intro_screen4)

        addBottomDots(0)
        introAdapter = IntroScreensViewpagerAdapter()
        binding.viewPager.adapter = introAdapter
        binding.viewPager.addOnPageChangeListener(this)

        binding.btnNext.setOnClickListener(View.OnClickListener {
            val current: Int = getItem(+1)
            if (current < layouts!!.size) { // move to next screen
                binding.viewPager.setCurrentItem(current)
            } else {
                showShortToast("launch login screen")
            }
        })
    }

    private fun getItem(i: Int): Int {
        return binding.viewPager.currentItem + i
    }

    private fun addBottomDots(currentPage: Int) {

        val circleActive = getDrawable(R.drawable.circle_without_border)
        val circleInactive = getDrawable(R.drawable.circle_with_border)

        binding.layoutDots.removeAllViews()

        dots = Array<ImageView>(4) {
            var circle = ImageView(this)
            circle.setImageDrawable(circleInactive)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(16, 16, 16, 16)
            circle.layoutParams = layoutParams
            binding.layoutDots?.addView(circle)
            circle
        }

        if (dots!!.isNotEmpty()) dots!![currentPage]?.setImageDrawable(circleActive)
    }

    override fun layoutId(): Int =
        R.layout.activity_intro

    inner class IntroScreensViewpagerAdapter: PagerAdapter()  {

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun getCount(): Int {
            return layouts?.size ?: 0
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            var view: View = `object` as View
            container.removeView(view)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
             val view =
                layoutInflater.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        addBottomDots(position)
        if (position == (layouts?.size?.minus(1))){
            binding.btnNext.text = "GET STARTED"
        } else {
            binding.btnNext.text = "NEXT"
        }
    }
}
