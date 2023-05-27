package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.data.local.Schedule
import com.dicoding.mentoring.databinding.ItemScheduleMeetingBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SubScheduleAdapter(private val schedules: List<Schedule>) :
    RecyclerView.Adapter<SubScheduleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SubScheduleAdapter.ViewHolder {
        val binding =
            ItemScheduleMeetingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubScheduleAdapter.ViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.binding.tvItemScheduleName.text = schedule.name

        val locale = Locale("id", "ID")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale)
        val outputFormat = SimpleDateFormat("HH:mm", locale)

        val fromDate = inputFormat.parse(schedule.fromDate!!)
        val formattedFromTime = outputFormat.format(fromDate!!)
        val toDate = inputFormat.parse(schedule.toDate!!)
        val formattedToTime = outputFormat.format(toDate!!)
        "$formattedFromTime - $formattedToTime".also { holder.binding.tvItemScheduleTime.text = it }
    }

    override fun getItemCount() = schedules.size

    inner class ViewHolder(val binding: ItemScheduleMeetingBinding) :
        RecyclerView.ViewHolder(binding.root)
}