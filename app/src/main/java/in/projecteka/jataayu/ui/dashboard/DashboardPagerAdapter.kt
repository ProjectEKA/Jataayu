package `in`.projecteka.jataayu.ui.dashboard

import `in`.projecteka.jataayu.consent.ui.fragment.ConsentHostFragment
import `in`.projecteka.jataayu.user.account.ui.fragment.UserAccountsFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

internal class DashboardPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val listOfFragments = arrayListOf<Fragment>(UserAccountsFragment.newInstance(), ConsentHostFragment.newInstance())

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size

}