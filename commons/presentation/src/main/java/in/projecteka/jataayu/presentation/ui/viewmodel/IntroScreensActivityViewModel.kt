package `in`.projecteka.jataayu.presentation.ui.viewmodel

import `in`.projecteka.jataayu.presentation.R
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager

class IntroScreensActivityViewModel(val preferenceRepository: PreferenceRepository): BaseViewModel(), ViewPager.OnPageChangeListener {
    var btnText = ObservableField<Int>(R.string.next)
    val addBottomDotsEvent = SingleLiveEvent<Int>()
    val getStartedEvent = SingleLiveEvent<Void>()
    val setViewpagerCurrentItemEvent = SingleLiveEvent<Int>()
    var layouts: Array<Int>? = null
    val circleActive =
        R.drawable.circle_without_border
    val circleInactive =
        R.drawable.circle_with_border
    val next = MutableLiveData<String>()
    val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    lateinit var dots: Array<ImageView>

    fun initialSetup() {
        layoutParams.setMargins(16, 16, 16, 16)
        layouts = arrayOf(
            R.layout.intro_screen1,
            R.layout.intro_screen2,
            R.layout.intro_screen3,
            R.layout.intro_screen4
        )
        addBottomDotsEvent.value = 0
        setViewpagerCurrentItemEvent.value = 0
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        addBottomDotsEvent.value = position
    }

    override fun onPageSelected(position: Int) {
        addBottomDotsEvent.value = position

        setViewpagerCurrentItemEvent.value = position

        if (position == (layouts?.size?.minus(1))){
            btnText.set(R.string.get_started)
        } else {
            btnText.set(R.string.next)
        }
    }

    fun onNextClick() {
        val current: Int = getItem()
        if (current < layouts!!.size) {
            setViewpagerCurrentItemEvent.value = current
        } else {
            preferenceRepository.shouldShowIntro = false
            getStartedEvent.call()
        }
    }

    private fun getItem(): Int {
        return setViewpagerCurrentItemEvent.value!!.plus(1)
    }
}