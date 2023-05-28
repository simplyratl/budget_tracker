package com.example.budget_tracker.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.budget_tracker.R
import com.example.budget_tracker.databinding.FragmentSettingsBinding
import com.example.budget_tracker.ui.auth.login.ui.LoginActivity
import com.example.budget_tracker.ui.settings.add_budget.AddBudgetActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
                ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userManager = UserManager.getInstance()

        val addBudgetButton = root.findViewById<CardView>(R.id.add_budget_settings)
        val logoutButton = root.findViewById<CardView>(R.id.logout_settings)

        addBudgetButton.setOnClickListener{
            val intent = Intent(activity, AddBudgetActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener{
            userManager.logout()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}