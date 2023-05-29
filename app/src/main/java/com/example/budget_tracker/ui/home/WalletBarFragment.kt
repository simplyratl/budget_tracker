package com.example.budget_tracker.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.budget_tracker.R
import java.text.DecimalFormat

class WalletBarFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wallet_bar, container, false)
        val budgetUserText: TextView = view.findViewById(R.id.budget_user_text)
        val budgetText:TextView = view.findViewById(R.id.text_budget)
        val monthlyIncome = view.findViewById<TextView>(R.id.home_budget_monthly_status_added)
        val monthlySpendings = view.findViewById<TextView>(R.id.home_budget_monthly_status_spent)


        val loggedInUser = UserManager.getInstance().getLoggedInUser()


        if (loggedInUser != null) {
            val decimalFormat = DecimalFormat("#0.00")
            val userName = loggedInUser.name
            val budget = decimalFormat.format(loggedInUser.budget)

            val earnings = decimalFormat.format(loggedInUser.earnings)
            val spendings = decimalFormat.format(loggedInUser.spendings)

            budgetUserText.text = "Welcome, $userName!"
            budgetText.text = "$budget€"

            monthlyIncome.text = "+" + earnings.toString() + "€"
            monthlySpendings.text = "-" + spendings.toString() + "€"
        } else {
            budgetUserText.text = "My Budget"
        }
        // Rest of your code
        return view
    }

}