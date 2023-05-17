package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.R
import com.dicoding.mentoring.data.local.Days
import com.dicoding.mentoring.databinding.InterestItemBinding

class DaysAdapter(private val days : List<Days>): RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysAdapter.DaysViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.interest_item,parent,false)
        return DaysViewHolder(view)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val days = days[position]
        holder.checkBox.text = days.name
        holder.checkBox.isChecked = days.isSelected

        holder.checkBox.setOnCheckedChangeListener{_, isChecked ->
            days.isSelected = isChecked
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    inner class DaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }


}