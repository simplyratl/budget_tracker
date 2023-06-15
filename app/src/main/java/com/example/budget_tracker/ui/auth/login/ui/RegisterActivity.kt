package com.example.budget_tracker.ui.auth.login.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budget_tracker.R
import com.example.budget_tracker.api.models.RetrofitInstance
import com.example.budget_tracker.api.models.models.RegisterRequest
import com.example.budget_tracker.utils.errorNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        val backButton = findViewById<LinearLayout>(R.id.back_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        backButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val nameEditText = findViewById<EditText>(R.id.register_name)
            val emailEditText = findViewById<EditText>(R.id.register_email)
            val budgetEditText = findViewById<EditText>(R.id.register_budget)
            val passwordEditText = findViewById<EditText>(R.id.register_password)

            val budgetText = budgetEditText.text.toString()


            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val budget = if (budgetText.isNotEmpty()) budgetText.toDouble() else 0.0
            val password = passwordEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()
            ) {
                errorNotification(this@RegisterActivity, "You have to fill out all fields")
                return@setOnClickListener
            }

            fun isFullNameEntered(): Boolean {
                val trimmedInput = name.trim()
                val nameAndSurname = trimmedInput.split(" ")
                return nameAndSurname.size == 2
            }

            fun isEmailEntered(): Boolean {
                return email.contains("@")
            }

            fun isBudgetValid(): Boolean {
                return budget < 9999
            }

            fun isPasswordValid(): Boolean {
                return password.length > 4
            }

            if (!isFullNameEntered()) {
                errorNotification(this@RegisterActivity, "You have to put your full name")
                return@setOnClickListener
            }

            if (!isEmailEntered()) {
                errorNotification(this@RegisterActivity, "You have to put correct email")
                return@setOnClickListener
            }

            if (!isBudgetValid()) {
                errorNotification(this@RegisterActivity, "Budget has to be less than 10.000")
                return@setOnClickListener
            }

            if (!isPasswordValid()) {
                errorNotification(
                    this@RegisterActivity,
                    "Password has to have more than 4 characters"
                )
                return@setOnClickListener
            }

            val request = RegisterRequest(name, email, budget, password)

            lifecycleScope.launch {
                try {
                    registerButton.isEnabled = false

                    // Call the suspend function inside the coroutine
                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.registerUser(request)
                    }

                    if (response.isSuccessful) {
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        showToast("Successfully registered")
                    } else {
                        // Login failed
                        // Handle the error condition according to your app's logic
                        showToast(response.message())
                    }
                } catch (e: Exception) {
                    // Handle any exceptions or errors
                } finally {
                    registerButton.isEnabled = true
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }
}