package com.dicoding.mentoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.dicoding.mentoring.data.local.ItemSchedule
import com.dicoding.mentoring.data.local.Schedule
import com.dicoding.mentoring.databinding.ItemScheduleMeetingBinding

class ItemScheduleAdapter(private val schedule : List<Schedule>) : RecyclerView.Adapter<ItemScheduleAdapter.ItemScheduleViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemScheduleAdapter.ItemScheduleViewHolder {
        val binding = ItemScheduleMeetingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemScheduleAdapter.ItemScheduleViewHolder,
        position: Int
    ) {
        val schedule = schedule[position]
        holder.binding.tvDateUpcomingMeeting.text = schedule.date

        val layoutManager = LinearLayoutManager(
            holder.binding.rvSubUpcomingMeeting.context,
            LinearLayoutManager.VERTICAL,
            false
        ).apply {
            initialPrefetchItemCount = schedule.mentoringSession.size
        }

        val subItemScheduleAdapter = SubItemScheduleAdapter(schedule.mentoringSession)
        holder.binding.rvSubUpcomingMeeting.layoutManager = layoutManager
        holder.binding.rvSubUpcomingMeeting.adapter = subItemScheduleAdapter
        holder.binding.rvSubUpcomingMeeting.setRecycledViewPool(viewPool)

    }

    override fun getItemCount(): Int {
        return schedule.size
    }

    inner class ItemScheduleViewHolder(val binding : ItemScheduleMeetingBinding) : RecyclerView.ViewHolder(binding.root)
}