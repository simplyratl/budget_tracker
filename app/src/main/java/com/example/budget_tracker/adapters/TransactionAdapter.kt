package com.example.budget_tracker.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_tracker.R
import com.example.budget_tracker.api.models.models.TransactionResponse
import java.text.SimpleDateFormat
import java.util.Locale

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

        holder.transactionNameTextView.text = transaction.title
        holder.transactionAmountTextView.text = transaction.amount.toString()
        holder.transactionDateTextView.text = formattedDate

        val backgroundDrawable = if (transaction.valid) {
            R.drawable.transactions_background
        } else {
            R.drawable.transactions_background_disabled
        }

        holder.cardView.apply {
            background = ContextCompat.getDrawable(context, backgroundDrawable)
            isEnabled = transaction.valid
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.transactionCardView)


        val transactionNameTextView: TextView = itemView.findViewById(R.id.transactionNameTextView)
        val transactionAmountTextView: TextView = itemView.findViewById(R.id.transactionAmountTextView)
        val transactionDateTextView: TextView = itemView.findViewById(R.id.transactionDateTextView)
    }

    private fun convertDate(createdAt: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = dateFormat.parse(createdAt)

        val currentTimeMillis = System.currentTimeMillis()
        val dateMillis = date?.time ?: 0

        return DateUtils.getRelativeTimeSpanString(dateMillis, currentTimeMillis, DateUtils.MINUTE_IN_MILLIS).toString()
    }
}
