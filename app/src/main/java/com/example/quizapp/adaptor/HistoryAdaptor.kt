package com.example.quizapp.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.CategoryitemBinding
import com.example.quizapp.databinding.HistoryitemBinding
import com.example.quizapp.model.HistoryModelClass
import com.example.quizapp.model.categoryModelClass
import java.sql.Timestamp
import java.util.Date

class HistoryAdaptor(var ListHistory: ArrayList<HistoryModelClass>) : RecyclerView.Adapter<HistoryAdaptor.HistoryCoinViewHolder>() {
    class HistoryCoinViewHolder(var binding: HistoryitemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCoinViewHolder {
        return HistoryCoinViewHolder(HistoryitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = ListHistory.size

    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {
        var timeStemp = Timestamp(ListHistory.get(position).timeAndDate.toLong())
        holder.binding.Time.text = Date(timeStemp.time).toString()
        holder.binding.status.text = if(ListHistory.get(position).isWithDrawal) {"Money Wthdrawal"} else {"+ Coin Added"}
        holder.binding.Coin.text = ListHistory[position].coin
    }
}