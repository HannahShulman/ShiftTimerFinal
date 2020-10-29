package com.shift.timer.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreakCalculationsDao {

    //minutes above limited will be calculated with higher rates
    @Query("SELECT minutesToDeduct FROM BreakCalculationsSetting WHERE workplaceId =:workplaceId ")
    fun breakMinutesToDeduct(workplaceId: Int): Flow<Int>

    @Query("UPDATE BreakCalculationsSetting SET minutesToDeduct=:minutesToDeduct WHERE workplaceId =:workplaceId")
    suspend fun setBreakMinutesToDeduct(workplaceId: Int, minutesToDeduct: Int)
}