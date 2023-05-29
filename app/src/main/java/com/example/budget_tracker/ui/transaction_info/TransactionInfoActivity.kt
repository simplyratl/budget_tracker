package com.example.budget_tracker.ui.transaction_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_tracker.R
import com.example.budget_tracker.adapters.ItemAdapter
import com.example.budget_tracker.adapters.TransactionAdapter
import com.example.budget_tracker.api.models.models.TransactionResponse
import com.example.budget_tracker.utils.convertDate
import com.example.budget_tracker.utils.successNotification
import java.text.DecimalFormat

class TransactionInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_transaction_info)

        val transaction = intent.getParcelableExtra<TransactionResponse>("transaction")

        val amount = findViewById<TextView>(R.id.transaction_info_amount)
        val address = findViewById<TextView>(R.id.transaction_info_address)
        val title = findViewById<TextView>(R.id.transaction_info_title)

        if (transaction != null) {
            if(transaction.items != null){
                val recyclerView: RecyclerView = findViewById(R.id.transaction_info_item_recycler)
                val adapter = ItemAdapter(transaction.items)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }
        }

        if(transaction != null){
            val decimalFormat = DecimalFormat("#0.00")
            amount.setText(decimalFormat.format(transaction.amount).toString() + "€")
            address.setText(transaction.address ?: convertDate(transaction.createdAt))
            title.setText(transaction.title)
        }
    }
}