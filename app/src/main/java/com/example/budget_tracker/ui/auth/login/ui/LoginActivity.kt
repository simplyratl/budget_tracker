package com.example.budget_tracker.ui.auth.login.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
import com.example.budget_tracker.utils.errorNotification
import com.example.budget_tracker.utils.successNotification

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_button)
        val createAccountButton = findViewById<TextView>(R.id.create_account)

        createAccountButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener{
            val emailEditText = findViewById<EditText>(R.id.emailInput)
            val passwordEditText = findViewById<EditText>(R.id.passwordInput)

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val loginRequest = LoginRequest(email, password)

            if(email.isEmpty() || password.isEmpty()){
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {

                    loginButton.isEnabled = false

                    // Call the suspend function inside the coroutine
                    val response = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.loginUser(loginRequest)
                    }

                    if (response.isSuccessful) {
                        // Login successful
                        val loggedInUser = response.body()

                        Log.d("LOGGING", loggedInUser.toString())

                        if (loggedInUser != null) {
                            UserManager.getInstance().setLoggedInUser(loggedInUser)
                        }

                        successNotification(this@LoginActivity, "Successful login")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        errorNotification(this@LoginActivity, response.message())
                    }
                } catch (e: Exception) {
                    // Handle any exceptions or errors
                } finally {
                    loginButton.isEnabled = true
                }
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}