package com.example.budget_tracker.ui.statistics.tabs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budget_tracker.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class WeekTabFragment : Fragment() {

    private lateinit var barChart: BarChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_week_tab, container, false)

        barChart = rootView.findViewById(R.id.week_bar_chart)
        setupBarChart()


        return rootView
    }

    private fun setupBarChart() {
        // Create dummy data entries
        val entries = listOf(
            BarEntry(1f, 50f),
            BarEntry(2f, 70f),
            BarEntry(3f, 30f),
            BarEntry(4f, 60f),
            BarEntry(5f, 80f)
        )

        // Create a dataset with the entries
        val dataSet = BarDataSet(entries, "Bar Data Set")
        dataSet.color = Color.BLUE

        // Create a BarData object and assign the dataset to it
        val barData = BarData(dataSet)

        // Customize the appearance of the chart
        barChart.apply {
            data = barData
            description.isEnabled = false // Hide the description
            setDrawValueAboveBar(true) // Display the value above each bar
            invalidate() // Refresh the chart

            // Set labels for each bar
            val entries = listOf(
                BarEntry(1f, 50f),
                BarEntry(2f, 70f),
                BarEntry(3f, 30f),
                BarEntry(4f, 60f),
                BarEntry(5f, 80f),
                BarEntry(6f, 40f),
                BarEntry(7f, 90f)
            )

            // Create a dataset with the entries
            val dataSet = BarDataSet(entries, "Bar Data Set")
            dataSet.color = Color.rgb(0, 123, 255)
            // Create a BarData object and assign the dataset to it
            val barData = BarData(dataSet)

            // Customize the appearance of the chart
            barChart.apply {
                data = barData
                description.isEnabled = false // Hide the description
                setDrawValueAboveBar(false) // Display the value below each bar
                invalidate() // Refresh the chart

                // Set labels for each bar
                xAxis.apply {
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return when (value.toInt()) {
                                1 -> "Ponedjeljak"
                                2 -> "Utorak"
                                3 -> "Srijeda"
                                4 -> "Četvrtak"
                                5 -> "Petak"
                                6 -> "Subota"
                                7 -> "Nedjelja"
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
}