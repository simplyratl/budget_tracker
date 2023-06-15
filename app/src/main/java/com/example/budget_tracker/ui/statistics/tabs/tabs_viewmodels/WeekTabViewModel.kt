import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budget_tracker.api.models.RetrofitInstance
import com.example.budget_tracker.api.models.repository.TransactionAPI
import kotlinx.coroutines.launch

class WeekTabViewModel : ViewModel() {
    private val transactionAPI: TransactionAPI =
        RetrofitInstance.api

    private val _weeklyTransactionStats = MutableLiveData<List<Double>>()
    val weeklyTransactionStats: LiveData<List<Double>> = _weeklyTransactionStats

    fun fetchWeeklyTransactionStatistics(id: String) {
        viewModelScope.launch {
            try {
                val response = transactionAPI.getStatisticsWeek(id)
                if (response.isSuccessful) {
                    val stats = response.body() ?: emptyList()
                    _weeklyTransactionStats.value = stats
                } else {
                    // Handle API error here
                }
            } catch (e: Exception) {
                // Handle exception here
            }
        }
    }
}
