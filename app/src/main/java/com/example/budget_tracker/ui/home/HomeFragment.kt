package com.example.budget_tracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_tracker.R
import com.example.budget_tracker.adapters.TransactionAdapter
import com.example.budget_tracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var transactionAdapter: TransactionAdapter


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val loggedInUser = UserManager.getInstance().getLoggedInUser()


        val recyclerView: RecyclerView = root.findViewById(R.id.transactionsRecyclerView)
        transactionAdapter = TransactionAdapter(emptyList()) // Pass an empty list initially
        recyclerView.adapter = transactionAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        transactionsViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]

        // Observe the transactions LiveData and update the adapter when it changes
        transactionsViewModel.transactions.observe(viewLifecycleOwner) { transactionResponse ->
            if (transactionResponse != null) {
                transactionAdapter.transactionList = transactionResponse
            }
            transactionAdapter.notifyDataSetChanged()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call the method to fetch transactions from the ViewModel
        transactionsViewModel.getAllTransactions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}