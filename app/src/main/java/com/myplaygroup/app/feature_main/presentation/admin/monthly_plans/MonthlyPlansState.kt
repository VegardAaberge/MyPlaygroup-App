package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan

data class MonthlyPlansState (
    val monthlyPlans: List<MonthlyPlan> = emptyList(),
    val showCreateMonthlyPlan: Boolean = false,

    val isLoading: Boolean = false
)