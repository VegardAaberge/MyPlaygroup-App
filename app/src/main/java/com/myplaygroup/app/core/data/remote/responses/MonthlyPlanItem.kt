package com.myplaygroup.app.core.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyPlanItem(
    val id: Long,
    val kidName: String,
    val paid: Boolean,
    val planName: String,
    val planPrice: Int
)