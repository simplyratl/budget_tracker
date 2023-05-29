package com.example.budget_tracker.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budget_tracker.api.models.RetrofitInstance
import com.example.budget_tracker.api.models.models.TransactionResponse
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel() {
    private val transactionAPI = RetrofitInstance.api

    private val _transactions = MutableLiveData<List<TransactionResponse>>()
    val transactions: LiveData<List<TransactionResponse>> get() = _transactions
    val loggedInUser = UserManager.getInstance().getLoggedInUser()

    var id = loggedInUser?.id ?: ""

    fun getAllTransactions() {
        viewModelScope.launch {
            try {
                val response = transactionAPI.getTransactionById(id)

                val responseBody = response.body()
                Log.d("TransactionData", "userid $id")
                Log.d("TransactionData", responseBody.toString()) // Log the response body
                if (response.isSuccessful) {
                    val transactionList = responseBody ?: emptyList()
                    _transactions.value = transactionList
                } else {
                    Log.e("TransactionData", "API call unsuccessful")
                }
            } catch (e: Exception) {
                Log.e("TransactionData", "API call failed", e)
            }
        }
    }
}
