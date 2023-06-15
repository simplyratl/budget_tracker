package com.example.budget_tracker.ui.statistics.tabs

import UserManager
import WeekTabViewModel
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

class WeekTabFragment() : Fragment() {
    private lateinit var viewModel: WeekTabViewModel

    private lateinit var barChart: BarChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[WeekTabViewModel::class.java]

        val rootView = inflater.inflate(R.layout.fragment_week_tab, container, false)
        val loggedInUser = UserManager.getInstance().getLoggedInUser()

        if (loggedInUser == null) {
            errorNotification(requireContext(), "No user found, so no data on graph will be shown")
            return rootView
        }

        barChart = rootView.findViewById(R.id.week_bar_chart)
        viewModel.fetchWeeklyTransactionStatistics(loggedInUser.id)

        viewModel.weeklyTransactionStats.observe(viewLifecycleOwner) { weeklyTransactionStats ->
            // Do something with the updated weeklyTransactionStats
            setupBarChart(weeklyTransactionStats)
        }

        return rootView
    }

    private fun setupBarChart(weeklyTransactionStats: List<Double>) {
        // Convert the weekly transaction stats to BarEntry objects
        val entries = weeklyTransactionStats.mapIndexed { index, value ->
            BarEntry((index + 1).toFloat(), value.toFloat())
        }

        // Create a dataset with the entries
        val dataSet = BarDataSet(entries, "Week Data")
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
                            1 -> "Mon"
                            2 -> "Tue"
                            3 -> "Wed"
                            4 -> "Thu"
                            5 -> "Fri"
                            6 -> "Sat"
                            7 -> "Sun"
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