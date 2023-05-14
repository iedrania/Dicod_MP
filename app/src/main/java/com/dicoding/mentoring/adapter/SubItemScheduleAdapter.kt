package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.data.local.ItemSchedule
import com.dicoding.mentoring.databinding.SubitemScheduleMeetingBinding

class SubItemScheduleAdapter(private val itemSchedule : List<ItemSchedule>) : RecyclerView.Adapter<SubItemScheduleAdapter.SubItemScheduleViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubItemScheduleAdapter.SubItemScheduleViewHolder {
        val binding = SubitemScheduleMeetingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SubItemScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SubItemScheduleAdapter.SubItemScheduleViewHolder,
        position: Int
    ) {
        val schedule = itemSchedule[position]
        holder.binding.tvMenteeName.text = schedule.name
        holder.binding.tvMentoringTime.text = schedule.time
    }

    override fun getItemCount(): Int {
        return itemSchedule.size
    }

    inner class SubItemScheduleViewHolder(val binding : SubitemScheduleMeetingBinding) : RecyclerView.ViewHolder(binding.root)
}