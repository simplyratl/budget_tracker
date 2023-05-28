import com.example.budget_tracker.api.models.models.UserResponse

class UserManager private constructor() {

    private var loggedInUser: UserResponse? = null

    fun setLoggedInUser(user: UserResponse) {
        loggedInUser = user
    }

    fun getLoggedInUser(): UserResponse? {
        return loggedInUser
    }

    fun updateBudget(amount: Double) {
        loggedInUser?.let { user ->
            val updatedBudget = user.budget - amount
            user.budget = updatedBudget
        }
    }

    companion object {
        private var instance: UserManager? = null

        fun getInstance(): UserManager {
            if (instance == null) {
                synchronized(UserManager::class.java) {
                    if (instance == null) {
                        instance = UserManager()
                    }
                }
            }
            return instance!!
        }
    }
}
