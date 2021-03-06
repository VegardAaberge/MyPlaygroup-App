package com.myplaygroup.app.feature_main.presentation.user.home

import com.myplaygroup.app.feature_main.domain.model.DailyClass
import java.time.LocalDate

data class HomeState (
    val dailyClasses: List<DailyClass> = emptyList(),
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = false
)