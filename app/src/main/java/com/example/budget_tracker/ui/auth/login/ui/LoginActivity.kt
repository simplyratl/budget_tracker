package com.example.budget_tracker.ui.auth.login.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.budget_tracker.MainActivity
import com.example.budget_tracker.R
import com.example.budget_tracker.api.models.RetrofitInstance
import com.example.budget_tracker.api.models.models.LoginRequest
import com.example.budget_tracker.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener{
            val emailEditText = findViewById<EditText>(R.id.emailInput)
            val passwordEditText = findViewById<EditText>(R.id.passwordInput)

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val loginRequest = LoginRequest(email, password)

            Log.d("LOGIN", email)
            Log.d("LOGIN", password)

            if(email.isEmpty() || password.isEmpty()){
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    // Call the suspend function inside the coroutine
                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.loginUser(loginRequest)
                    }

                    if (response.isSuccessful) {
                        // Login successful
                        val loggedInUser = response.body()
                        if (loggedInUser != null) {
                            UserManager.getInstance().setLoggedInUser(loggedInUser)
                        }

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Login failed
                        // Handle the error condition according to your app's logic
                        if (response.code() == 404) {
                            // User not found
                            showToast("No user found")
                        } else if(response.code() == 403){
                            showToast("Incorrect credentials")
                        }
                    }
                } catch (e: Exception) {
                    // Handle any exceptions or errors
                }
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}