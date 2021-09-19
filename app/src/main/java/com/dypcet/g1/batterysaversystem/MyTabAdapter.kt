package com.dypcet.g1.batterysaversystem

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dypcet.g1.batterysaversystem.alarmsettings.AlarmSettingsFragment
import com.dypcet.g1.batterysaversystem.alertsettings.AlertSettingsFragment
import com.dypcet.g1.batterysaversystem.applist.AppListContainerFragment

class MyTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int) = when (position) {
        0 -> AppListContainerFragment()
        1 -> AlarmSettingsFragment()
        2 -> AlertSettingsFragment()
        else -> Fragment()
    }
}