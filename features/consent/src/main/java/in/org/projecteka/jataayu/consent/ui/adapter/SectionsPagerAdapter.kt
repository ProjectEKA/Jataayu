package `in`.org.projecteka.jataayu.consent.ui.adapter

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentListFragment
import `in`.org.projecteka.jataayu.consent.ui.fragment.ConsentRequestFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private val TAB_TITLES = arrayOf(
    R.string.tab_requests,
    R.string.tab_consents
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val REQUESTS_INDEX = 0
    private val TABS_COUNT = 2

    override fun getItem(position: Int): Fragment {
        if (position == REQUESTS_INDEX) {
            return ConsentRequestFragment.newInstance()
        } else {
            return ConsentListFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TABS_COUNT
    }
}