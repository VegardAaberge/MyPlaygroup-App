package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.DayOfWeek

@Serializable
@Entity
data class DailyClassEntity(
    @PrimaryKey val id: Long,
    val cancelled: Boolean,
    val classType: String,
    val date: String,
    val endTime: String,
    val startTime: String,
    val dayOfWeek: DayOfWeek,
    val kids: List<String>,

    @Transient
    val modified: Boolean = id == -1L
)