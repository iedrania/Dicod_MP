package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mentoring.data.local.GroupedSchedule
import com.dicoding.mentoring.databinding.ItemScheduleDayBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScheduleAdapter(private val groupedSchedules: List<GroupedSchedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ScheduleAdapter.ViewHolder {
        val binding =
            ItemScheduleDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ScheduleAdapter.ViewHolder, position: Int
    ) {
        val groupedSchedule = groupedSchedules[position]

        val locale = Locale("id", "ID")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", locale)
        val date: Date = inputFormat.parse(groupedSchedule.fromDate) as Date
        val formattedDate: String = outputFormat.format(date)
        holder.binding.tvScheduleDate.text = formattedDate

        holder.binding.rvSubSchedule.adapter = SubScheduleAdapter(groupedSchedule.schedules)
        holder.binding.rvSubSchedule.layoutManager = LinearLayoutManager(
            holder.binding.rvSubSchedule.context, LinearLayoutManager.VERTICAL, false
        ).apply {
            initialPrefetchItemCount = groupedSchedule.schedules.size
        }
        holder.binding.rvSubSchedule.setRecycledViewPool(viewPool)
    }

    override fun getItemCount() = groupedSchedules.size

    inner class ViewHolder(val binding: ItemScheduleDayBinding) :
        RecyclerView.ViewHolder(binding.root)
}