package com.shift.timer.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyStartingCalculationsSettingDao {

    //minutes above limited will be calculated with higher rates
    @Query("SELECT dayOfMonth FROM MonthlyStartingCalculationsSetting WHERE workplaceId =:workplaceId ")
    fun dayStartingCalculation(workplaceId: Int): Flow<Int>

    //minutes above limited will be calculated with higher rates
    @Query("UPDATE MonthlyStartingCalculationsSetting SET dayOfMonth=:dayOfMonth WHERE workplaceId =:workplaceId")
    suspend fun startMonthlyCalculationCycle(workplaceId: Int, dayOfMonth: Int)
}