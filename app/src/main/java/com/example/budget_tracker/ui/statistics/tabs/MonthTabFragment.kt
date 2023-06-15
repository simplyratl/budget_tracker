package com.example.budget_tracker.ui.statistics.tabs

import MonthTabViewModel
import UserManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.budget_tracker.R
import com.example.budget_tracker.utils.errorNotification
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class MonthTabFragment : Fragment() {
    private lateinit var viewModel: MonthTabViewModel

    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[MonthTabViewModel::class.java]

        val rootView = inflater.inflate(R.layout.fragment_month_tab, container, false)
        val loggedInUser = UserManager.getInstance().getLoggedInUser()

        if (loggedInUser == null) {
            errorNotification(requireContext(), "No user found, so no data on graph will be shown")
            return rootView
        }

        barChart = rootView.findViewById(R.id.month_bar_chart)
        viewModel.fetchMonthlyTransactionStatistics(loggedInUser.id)


        viewModel.monthTransactionStats.observe(viewLifecycleOwner) { monthTransactionStats ->
            // Do something with the updated weeklyTransactionStats
            setupBarChart(monthTransactionStats)
        }

        // Inflate the layout for this fragment
        return rootView
    }

    private fun setupBarChart(monthTransactionStats: List<Double>) {
        // Convert the weekly transaction stats to BarEntry objects
        val entries = monthTransactionStats.mapIndexed { index, value ->
            BarEntry((index + 1).toFloat(), value.toFloat())
        }

        // Create a dataset with the entries
        val dataSet = BarDataSet(entries, "Monthly Data")
        dataSet.color = Color.rgb(0, 123, 255)

        // Create a BarData object and assign the dataset to it
        val barData = BarData(dataSet)

        // Customize the appearance of the chart
        barChart.apply {
            data = barData
            description.isEnabled = false // Hide the description
            setDrawValueAboveBar(true) // Display the value above each bar
            invalidate() // Refresh the chart

            // Set labels for each bar
            xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value.toInt()) {
                            1 -> "Week 1"
                            2 -> "Week 2"
                            3 -> "Week 3"
                            4 -> "Week 4"
                            else -> ""
                        }
                    }
                }
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
            }

            // Set color and size for the labels
            val labelTextColor = Color.WHITE
            val labelTextSize = 12f

            xAxis.apply {
                textColor = labelTextColor
                textSize = labelTextSize
            }
        }
    }
}