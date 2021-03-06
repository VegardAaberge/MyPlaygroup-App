package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.StandardPlan
import kotlinx.coroutines.flow.Flow

interface MonthlyPlansRepository {

    suspend fun getMonthlyPlans(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<MonthlyPlan>>>

    suspend fun getStandardPlans(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<StandardPlan>>>

    suspend fun addMonthlyPlanToDatabase(
        monthlyPlan: MonthlyPlan
    ) : Resource<MonthlyPlan>

    suspend fun uploadMonthlyPlans(
        unsyncedMonthlyPlans: List<MonthlyPlan>
    ): Flow<Resource<List<MonthlyPlan>>>

    suspend fun updateClassInfo() : Resource<List<MonthlyPlan>>

    suspend fun updateClassInfo(monthlyPlan: MonthlyPlan) :  Resource<List<MonthlyPlan>>
}