package com.dicoding.mentoring.data.local

import android.content.ClipData.Item
import java.sql.Time

data class Schedule (
    val date : String?,
    val mentoringSession: List<ItemSchedule>
    )

data class ItemSchedule(
    val name : String?,
    val time : String?
)
