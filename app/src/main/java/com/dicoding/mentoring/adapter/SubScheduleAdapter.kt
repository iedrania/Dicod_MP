package com.dicoding.mentoring.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
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

        holder.itemView.setOnClickListener {
            schedule.meetingId?.let { meetingId ->
                startGoogleMeet(meetingId, holder.itemView.context)
            }
        }
    }

    private fun startGoogleMeetInBrowser(meetingId: String, context: Context) {
        val googleMeetUrl = "https://meet.google.com/$meetingId"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMeetUrl))
        context.startActivity(intent)
    }

    private fun startGoogleMeet(meetingId: String, context: Context) {
        val googleMeetPackage = "com.google.android.apps.tachyon"
        val googleMeetUrl = "https://meet.google.com/$meetingId"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        val packageManager = context.packageManager
        val resolveInfo = packageManager.resolveActivity(intent, 0)
        val isAppInstalled =
            resolveInfo != null && resolveInfo.activityInfo.packageName == googleMeetPackage

        if (isAppInstalled) {
            intent.`package` = googleMeetPackage
            intent.data = Uri.parse("https://meet.google.com/$meetingId")
        } else {
            intent.data = Uri.parse(googleMeetUrl)
        }

        context.startActivity(intent)
    }

    override fun getItemCount() = schedules.size

    inner class ViewHolder(val binding: ItemScheduleMeetingBinding) :
        RecyclerView.ViewHolder(binding.root)
}