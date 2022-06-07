package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.util.*

@Serializable
@Entity
data class MonthlyPlanEntity(
    @PrimaryKey val id: Long,
    val paid: Boolean,
    val planName: String,
    val daysOfWeek: List<String>,
    val planPrice: Long,
    val kidName: String,
)