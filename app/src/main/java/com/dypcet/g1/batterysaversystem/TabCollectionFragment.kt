package com.dypcet.g1.batterysaversystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabCollectionFragment : Fragment() {

    private lateinit var myTabsAdapter: MyTabAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myTabsAdapter = MyTabAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = myTabsAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_baseline_apps_24
                    )
                }
                1 -> {
                    tab.icon = AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_baseline_alarm_24
                    )
                }
                2 -> {
                    tab.icon = AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_baseline_battery_alert_24
                    )
                }
            }
        }.attach()
    }
}