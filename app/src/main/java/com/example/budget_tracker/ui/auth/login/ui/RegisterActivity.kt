package com.example.budget_tracker.ui.auth.login.ui

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
import com.example.budget_tracker.api.models.models.RegisterRequest
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

        backButton.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener{
            val nameEditText = findViewById<EditText>(R.id.register_name)
            val emailEditText = findViewById<EditText>(R.id.register_email)
            val budgetEditText = findViewById<EditText>(R.id.register_budget)
            val passwordEditText = findViewById<EditText>(R.id.register_password)

            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val budget = budgetEditText.text.toString().toDouble()
            val password = passwordEditText.text.toString()

            val request = RegisterRequest(name, email, budget, password)

            lifecycleScope.launch {
                try {
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
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }
}