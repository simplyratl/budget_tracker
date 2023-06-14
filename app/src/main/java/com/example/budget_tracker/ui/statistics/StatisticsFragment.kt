package com.example.budget_tracker.ui.statistics

import PagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.budget_tracker.databinding.FragmentStatisticsBinding
import com.example.budget_tracker.ui.statistics.tabs.MonthTabFragment
import com.example.budget_tracker.ui.statistics.tabs.WeekTabFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)

        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPager2 = binding.statisticsViewPager
        tabLayout = binding.statisticsTabLayout

        val adapter = PagerAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        adapter.addFragment(WeekTabFragment(), "Week")
        adapter.addFragment(MonthTabFragment(), "Month")

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Week"
                1 -> "Month"
                2 -> "Year"
                else -> ""
            }
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}