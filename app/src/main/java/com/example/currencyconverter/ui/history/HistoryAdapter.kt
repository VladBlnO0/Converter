package com.example.currencyconverter.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R

class HistoryAdapter(private val items: List<HistoryModel>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    var onDeleteClicked: ((HistoryModel) -> Unit)? = null
    var onReturnClicked: ((HistoryModel) -> Unit)? = null
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyTextView: TextView = itemView.findViewById(R.id.currencyText)
        val valueTextView: TextView = itemView.findViewById(R.id.valueText)
        val convertedValueView: TextView = itemView.findViewById(R.id.convertedValueText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]

        holder.currencyTextView.text = "${item.fromCurrencyCode.uppercase()} → ${item.toCurrencyCode.uppercase()}"
        holder.valueTextView.text = item.valueText
        holder.convertedValueView.text = item.convertedValueText

        holder.itemView.findViewById<Button>(R.id.return_btn).setOnClickListener {
            onReturnClicked?.invoke(item)
        }
        holder.itemView.findViewById<Button>(R.id.delete_btn).setOnClickListener {
            onDeleteClicked?.invoke(item)
        }
    }

    override fun getItemCount(): Int = items.size
}

