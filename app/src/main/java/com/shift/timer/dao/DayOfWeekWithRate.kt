package com.shift.timer.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

data class DayOfWeekWithRate(val dayOfWeek: Int, val rate: Int)

@Dao
interface RatePerDaySettingDao {

    @Query("""UPDATE RatePerDaySetting SET rate=:rate WHERE dayOfWeek=:dayOfWeek AND workplaceId =:workpalceId""")
    fun updateRatePerDay(workpalceId: Int, dayOfWeek: Int, rate: Int)

    @Transaction
    suspend fun updateRatePerDay(rates: List<DayOfWeekWithRate>) {
        for (i in rates) {
            updateRatePerDay(-1, i.dayOfWeek, i.rate)
        }
    }
}