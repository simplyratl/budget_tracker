package com.example.budget_tracker.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budget_tracker.api.models.RetrofitInstance
import kotlinx.coroutines.launch

class StatisticsViewModel() : ViewModel() {
    private val transactionAPI = RetrofitInstance.api

    private val _weeklyTransactionStats = MutableLiveData<ArrayList<Double>>()
    val weeklyTransactionStats: LiveData<ArrayList<Double>> = _weeklyTransactionStats

    private val _monthlyTransactionStats = MutableLiveData<List<Double>>()
    val monthlyTransactionStats: LiveData<List<Double>> = _monthlyTransactionStats


    fun fetchWeeklyTransactionStatistics(id: String) {
        viewModelScope.launch {
            try {
                val response = transactionAPI.getStatisticsWeek(id)
                if (response.isSuccessful) {
                    val stats = response.body() ?: emptyList()
                    if (stats.size >= 4) {
                        val weeklyStats = ArrayList<Double>().apply {
                            addAll(stats.subList(0, 4))
                        }
                        _weeklyTransactionStats.value = weeklyStats
                    }
                } else {
//                    errorNotification(context, "Error while displaying graph")
                }
            } catch (e: Exception) {
//                errorNotification(context, "Unknown error while displaying graph")
            }
        }
    }

    // Function to fetch month statistics
    fun fetchMonthlyTransactionStatistics(id: String) {
        viewModelScope.launch {
            try {
                val response = transactionAPI.getStatisticsMonth(id)
                if (response.isSuccessful) {
                    val stats = response.body()
                    _monthlyTransactionStats.value = stats ?: emptyList()
                } else {
                    // Handle the API error
                }
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }
}