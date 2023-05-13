package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.data.local.Days
import com.dicoding.mentoring.databinding.ItemDaysChipBinding

class DaysAdapter(private val days : List<Days>): RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysAdapter.DaysViewHolder {
        val binding = ItemDaysChipBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val days = days[position]
        holder.binding.itemChipDay.text = days.name
        holder.binding.itemChipDay.isChecked = days.isSelected

        holder.binding.itemChipDay.setOnCheckedChangeListener{_, isChecked ->
            days.isSelected = isChecked
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    inner class DaysViewHolder(val binding: ItemDaysChipBinding) : RecyclerView.ViewHolder(binding.root)


}