package com.example.budget_tracker.ui.home

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
import com.example.budget_tracker.api.models.models.AddTransationRequest
import com.example.budget_tracker.api.models.models.ScanQRCodeRequest
import com.example.budget_tracker.api.models.models.ScannedQRCodeResponse
import com.example.budget_tracker.utils.errorNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_transaction)

        val userManager = UserManager.getInstance()
        val loggedInUser = userManager.getLoggedInUser()

        val backButton = findViewById<LinearLayout>(R.id.back_button)
        val submitButton = findViewById<Button>(R.id.add_transaction_button)

        val nameEditText = findViewById<EditText>(R.id.transaction_name_input)
        val amountEditText = findViewById<EditText>(R.id.transaction_amount_input)

        val scannedTransaction = intent.getParcelableExtra<ScannedQRCodeResponse>("scannedTransaction")
        if (scannedTransaction != null) {
            nameEditText.setText(scannedTransaction.title)
            amountEditText.setText(scannedTransaction.amount.toString())
        }

        backButton.setOnClickListener{
            val intent = Intent(this@AddTransactionActivity, MainActivity::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener{

            val title = nameEditText.text.toString()
            val amount = amountEditText.text.toString().toDouble()
            val userId = loggedInUser?.id.toString()

            if(title.isEmpty() || amount.isNaN() || userId.isEmpty()){
                errorNotification(this@AddTransactionActivity, "Please fill all inputs")
                return@setOnClickListener
            }

            if(amount > 999999){
                errorNotification(this@AddTransactionActivity, "You can't spend more than 1 million euros")
                return@setOnClickListener
            }

            val request = AddTransationRequest(amount, userId, title, null, null)

            lifecycleScope.launch {
                try {
                    submitButton.isEnabled = false

                    // Call the suspend function inside the coroutine
                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.createTransaction(request)
                    }

                    if (response.isSuccessful) {
                        // Login successful
                        val addedTransaction = response.body()
                        if (addedTransaction != null) {
                            if (loggedInUser != null && addedTransaction.amount < loggedInUser.budget) {
                                userManager.updateBudget(addedTransaction.amount)
                            }
                        }

                        val intent = Intent(this@AddTransactionActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        errorNotification(this@AddTransactionActivity, response.message())

                    }
                } catch (e: Exception) {
                    errorNotification(this@AddTransactionActivity, e.message as String)
                    // Handle any exceptions or errors
                } finally {
                    submitButton.isEnabled = true
                }
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@AddTransactionActivity, message, Toast.LENGTH_SHORT).show()
    }
}