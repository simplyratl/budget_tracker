package com.example.budget_tracker.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_tracker.R
import com.example.budget_tracker.api.models.models.Item
import com.example.budget_tracker.api.models.models.TransactionResponse
import com.example.budget_tracker.ui.transaction_info.TransactionInfoActivity
import com.example.budget_tracker.utils.convertDate
import java.text.DecimalFormat

class ItemAdapter(var itemList: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bough_item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.itemAmount.setText(item.price.toString() + "€")
        holder.itemName.setText(item.name)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemAmount: TextView = itemView.findViewById(R.id.item_amount)
    }

}