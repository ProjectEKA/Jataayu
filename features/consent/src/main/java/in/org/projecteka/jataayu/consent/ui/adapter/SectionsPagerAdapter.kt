package `in`.org.projecteka.jataayu.consent.ui.adapter

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private val TAB_TITLES = arrayOf(
//    R.string.tab_requests,
    R.string.tab_consents
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
//        if (position == 0){
//            return RequestFragment.newInstance()
//        } else {
            return ConsentFragment.newInstance()
//        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 1
    }
}