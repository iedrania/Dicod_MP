package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.R
import com.dicoding.mentoring.data.local.InterestItem
import com.dicoding.mentoring.data.local.InterestResponse

class InterestAdapter(private val interests: List<InterestItem>) :
    RecyclerView.Adapter<InterestAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.interest_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val interest = interests[position]
        holder.checkBox.text = interest.name
        holder.checkBox.isChecked = interest.isChecked
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            interest.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int {
        return interests.size
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

}