package com.example.budget_tracker.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.budget_tracker.R

class WalletBarFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_wallet_bar, container, false)
        val budgetUserText: TextView = view.findViewById(R.id.budget_user_text)
        val budgetText:TextView = view.findViewById(R.id.text_budget)

        val loggedInUser = UserManager.getInstance().getLoggedInUser()


        if (loggedInUser != null) {
            val userName = loggedInUser.name
            val budget = loggedInUser.budget

            budgetUserText.text = "Welcome, $userName!"
            budgetText.text = "$budget€"
        } else {
            budgetUserText.text = "My Budget"
        }
        // Rest of your code
        return view
    }

}