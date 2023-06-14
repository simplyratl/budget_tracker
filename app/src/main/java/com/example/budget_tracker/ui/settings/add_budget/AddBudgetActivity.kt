package com.example.budget_tracker.ui.settings.add_budget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.budget_tracker.MainActivity
import com.example.budget_tracker.R
import com.example.budget_tracker.api.models.RetrofitInstance
import com.example.budget_tracker.api.models.models.AddBudgetUserRequest
import com.example.budget_tracker.api.models.models.AddTransationRequest
import com.example.budget_tracker.utils.errorNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBudgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_budget)

        val userManager = UserManager.getInstance()
        val loggedInUser = userManager.getLoggedInUser()

        val backButton = findViewById<LinearLayout>(R.id.back_button)
        val submitButton = findViewById<Button>(R.id.add_budget_button)

        backButton.setOnClickListener{
            val intent = Intent(this@AddBudgetActivity, MainActivity::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener{

            val amountEditText = findViewById<EditText>(R.id.budget_amount_input)

            val amount = amountEditText.text.toString().toDouble()
            val id = loggedInUser?.id.toString()

            if(amount.isNaN() || id.isEmpty()){
                return@setOnClickListener
            }

            if(amount >= 10000){
                errorNotification(this@AddBudgetActivity, "You can't add more than 10 thousand of income")
                return@setOnClickListener
            }

            if (loggedInUser != null) {
                if(amount + loggedInUser.budget > 10000){
                    errorNotification(this@AddBudgetActivity, "You can't add more than 10 thousand of income in total")
                    return@setOnClickListener
                }
            }

            val request = AddBudgetUserRequest(id, amount)

            lifecycleScope.launch {
                try {
                    // Call the suspend function inside the coroutine
                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.addBudgetToUser(request)
                    }

                    if (response.isSuccessful) {
                        // Login successful
                        val addedTransaction = response.body()
                        if (addedTransaction != null) {
                            if (loggedInUser != null) {
                                userManager.addToBudget(amount)
                            }
                        }

                        val intent = Intent(this@AddBudgetActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Login failed
                        // Handle the error condition according to your app's logic
                        showToast(response.message())
                    }
                } catch (e: Exception) {
                    // Handle any exceptions or errors
                }
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@AddBudgetActivity, message, Toast.LENGTH_SHORT).show()
    }
}