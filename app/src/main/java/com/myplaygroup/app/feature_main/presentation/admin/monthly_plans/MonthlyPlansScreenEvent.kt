package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

sealed class MonthlyPlansScreenEvent {
    object RefreshData : MonthlyPlansScreenEvent()
    data class CreateMonthlyPlanDialog(val show: Boolean) : MonthlyPlansScreenEvent()
}
