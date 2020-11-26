package com.shift.timer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shift.timer.model.WageSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface AdditionalHoursSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWageSetting(wage: WageSetting)

    @Query("SELECT * FROM WageSetting WHERE workplaceId =:id")
    fun getWorkplaceById(id: Int): WageSetting

    //minutes above limited will be calculated with higher rates
    @Query("SELECT regularRateMinutes FROM AdditionalHoursSetting WHERE workplaceId =:workplaceId ")
    fun getLimitedMinutesForRegularPayment(workplaceId: Int): Flow<Int>

    @Query("UPDATE AdditionalHoursSetting SET  regularRateMinutes=:minutes WHERE workplaceId =:workplaceId ")
    suspend fun setMinutesPaidRegularRate(workplaceId: Int, minutes: Int)
}