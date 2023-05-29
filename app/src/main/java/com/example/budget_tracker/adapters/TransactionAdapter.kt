package com.example.budget_tracker.adapters

import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_tracker.R
import com.example.budget_tracker.api.models.models.Item
import com.example.budget_tracker.api.models.models.TransactionResponse
import com.example.budget_tracker.ui.transaction_info.TransactionInfoActivity
import com.example.budget_tracker.utils.convertDate
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.log

class TransactionAdapter(var transactionList: List<TransactionResponse>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList[position]
        val formattedDate = convertDate(transaction.createdAt)
        val decimalFormat = DecimalFormat("#0.00")

        Log.d("TRANSACTION ONE", transaction.toString())

        holder.transactionNameTextView.text = transaction.title
        holder.transactionAmountTextView.text = decimalFormat.format(transaction.amount).toString() + "€"
        holder.transactionDateTextView.text = formattedDate

        val backgroundDrawable = if (transaction.valid) {
            if(transaction.addedIncome == true) {
                Log.d("TRANSACTION ONE", "PROSLO")
                R.drawable.transactions_background_added_income
            } else{
                R.drawable.transactions_background
            }
        } else {
            R.drawable.transactions_background_disabled
        }


        holder.cardView.apply {
            background = ContextCompat.getDrawable(context, backgroundDrawable)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                var transaction = transactionList[adapterPosition]
                val intent = Intent(context, TransactionInfoActivity::class.java)
                intent.putExtra("transaction", transaction)
                context.startActivity(intent)
            }
        }

        val cardView: CardView = itemView.findViewById(R.id.transactionCardView)

        val transactionNameTextView: TextView = itemView.findViewById(R.id.transactionNameTextView)
        val transactionAmountTextView: TextView = itemView.findViewById(R.id.transactionAmountTextView)
        val transactionDateTextView: TextView = itemView.findViewById(R.id.transactionDateTextView)
    }
}
