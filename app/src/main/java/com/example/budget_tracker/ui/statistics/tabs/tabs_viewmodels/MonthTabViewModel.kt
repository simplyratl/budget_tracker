import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budget_tracker.api.models.RetrofitInstance
import com.example.budget_tracker.api.models.repository.TransactionAPI
import kotlinx.coroutines.launch

class MonthTabViewModel : ViewModel() {
    private val transactionAPI: TransactionAPI =
        RetrofitInstance.api

    private val _monthTransactionStats = MutableLiveData<List<Double>>()
    val monthTransactionStats: LiveData<List<Double>> = _monthTransactionStats

    fun fetchMonthlyTransactionStatistics(id: String) {
        viewModelScope.launch {
            try {
                val response = transactionAPI.getStatisticsMonth(id)
                if (response.isSuccessful) {
                    val stats = response.body() ?: emptyList()
                    _monthTransactionStats.value = stats
                } else {
                    // Handle API error here
                }
            } catch (e: Exception) {
                // Handle exception here
            }
        }
    }
}
