package com.shift.timer.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.shift.timer.extensions.toRoomIntValue
import com.shift.timer.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertShift(shift: Shift)

    @Query("SELECT * FROM shift WHERE `end` IS NULL")
    fun getCurrentShift(): Flow<Shift?>

    @Query("SELECT * FROM shift WHERE id =:shiftId")
    fun getShiftById(shiftId: Int): Flow<Shift>

    @Update
    suspend fun endShift(shift: Shift): Int

    @Query("UPDATE shift SET rate =:rate, note =:note, bonus=:bonus,  start = :start, `end` = :end  WHERE id =:id")
    suspend fun updateShift(id: Int, start: Long, end: Long?, rate: Int, note: String, bonus: Int);
}

@Dao
interface WorkplaceDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertWorkplace(workplace: Workplace)

    @Query("SELECT * FROM workplace")
    fun getAllWorkplaces(): Flow<List<Workplace>>

    @Query("SELECT * FROM workplace WHERE id =:id")
    fun getWorkplaceById(id: Int): Flow<Workplace>

}

@Dao
interface WageSettingDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertWageSetting(wage: WageSetting)

    @Query("SELECT * FROM WageSetting WHERE id =:id")
    fun getWorkplaceById(id: Int): WageSetting

    @Query("SELECT wage FROM WageSetting WHERE workplaceId =:workplaceId ")
    fun getHourlyPayment(workplaceId: Int): Flow<Int>

    @Query("UPDATE wagesetting SET  wage=:cents WHERE id =:workplaceId")
    suspend fun setHourlyPayment(workplaceId: Int, cents: Int)

}

@Dao
interface AdditionalHoursSettingDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertWageSetting(wage: WageSetting)

    @Query("SELECT * FROM WageSetting WHERE id =:id")
    fun getWorkplaceById(id: Int): WageSetting

    //minutes above limited will be calculated with higher rates
    @Query("SELECT regularRateMinutes FROM AdditionalHoursSetting WHERE workplaceId =:workplaceId ")
    fun getLimitedMinutesForRegularPayment(workplaceId: Int): Flow<Int>

    //minutes above limited will be calculated with higher rates
    @Query("UPDATE AdditionalHoursSetting SET  regularRateMinutes=:minutes WHERE id =:workplaceId ")
    suspend fun setMinutesPaidRegularRate(workplaceId: Int, minutes: Int)
}

@Dao
interface TravelExpensesDao {

    @Insert(onConflict = REPLACE)
    fun insertTravelExpenseSetting(setting: TravelExpensesSetting)

    //minutes above limited will be calculated with higher rates
    @Query("SELECT * FROM TravelExpensesSetting WHERE workplaceId =:workplaceId ")
    fun getTravellingExpenseSetting(workplaceId: Int): Flow<TravelExpensesSetting>

    //minutes above limited will be calculated with higher rates
    @Query("SELECT shouldCalculate FROM TravelExpensesSetting WHERE workplaceId =:workplaceId ")
    fun shouldCalculateTravelExpense(workplaceId: Int): Flow<Boolean>


    @Query("UPDATE TravelExpensesSetting SET shouldCalculate=:shouldCalculate,  singleTravelExpense=:singleExpense WHERE id =:workplaceId")
    suspend fun updateTravelExpenseSetting(
        workplaceId: Int,
        shouldCalculate: Int,
        singleExpense: Int
    )
}

@Dao
interface BreakCalculationsDao {

    //minutes above limited will be calculated with higher rates
    @Query("SELECT minutesToDeduct FROM BreakCalculationsSetting WHERE workplaceId =:workplaceId ")
    fun breakMinutesToDeduct(workplaceId: Int): Flow<Int>

    @Query("UPDATE BreakCalculationsSetting SET minutesToDeduct=:minutesToDeduct WHERE id =:workplaceId")
    suspend fun setBreakMinutesToDeduct(workplaceId: Int, minutesToDeduct: Int)
}

@Dao
interface MonthlyStartingCalculationsSettingDao {

    //minutes above limited will be calculated with higher rates
    @Query("SELECT dayOfMonth FROM MonthlyStartingCalculationsSetting WHERE workplaceId =:workplaceId ")
    fun dayStartingCalculation(workplaceId: Int): Flow<Int>

    //minutes above limited will be calculated with higher rates
    @Query("UPDATE MonthlyStartingCalculationsSetting SET dayOfMonth=:dayOfMonth WHERE id =:workplaceId")
    suspend fun startMonthlyCalculationCycle(workplaceId: Int, dayOfMonth: Int)
}

data class DayOfWeekWithRate(val dayOfWeek: Int, val rate: Int)

@Dao
interface RatePerDaySettingDao {

    @Query("UPDATE RatePerDaySetting SET rate=:rate WHERE dayOfWeek=:dayOfWeek AND workplaceId =:workpalceId")
    fun updateRatePerDay(workpalceId: Int, dayOfWeek: Int, rate: Int)

    @Transaction
    suspend fun updateRatePerDay(rates: List<DayOfWeekWithRate>) {
        for (i in rates) {
            updateRatePerDay(-1, i.dayOfWeek, i.rate)
        }
    }
}

@Dao
interface NotifySettingDao {

    @Insert(onConflict = REPLACE)
    fun insert(setting: NotifyOnArrivalSetting)

    @Insert(onConflict = REPLACE)
    fun insert(setting: NotifyOnLeaveSetting)

    @Query("UPDATE NotifyOnArrivalSetting SET shouldNotify=:notify WHERE workplaceId =:workpalceId")
    suspend fun updateNotifyOnArrive(workpalceId: Int, notify: Int)

    @Query("UPDATE NotifyOnLeaveSetting SET shouldNotify=:notify WHERE workplaceId =:workpalceId")
    suspend fun updateNotifyOnLeave(workpalceId: Int, notify: Int)

    @Query("SELECT shouldNotify FROM NotifyOnArrivalSetting WHERE workplaceId =:workpalceId")
    fun notifyOnArrive(workpalceId: Int): Flow<Boolean>

    @Query("SELECT shouldNotify FROM NotifyOnLeaveSetting WHERE workplaceId =:workpalceId")
    fun notifyOnLeave(workpalceId: Int): Flow<Boolean>

    @Query("SELECT * FROM NotifyAfterShiftSetting WHERE workplaceId =:workpalceId")
    fun getNotifyOnShiftCompletionSetting(workpalceId: Int): Flow<NotifyAfterShiftSetting>

    @Query("SELECT shouldNotify FROM NotifyAfterShiftSetting WHERE workplaceId =:workpalceId")
    fun shouldNotifyOnShiftCompletion(workpalceId: Int): Flow<Boolean>

//    @Query("UPDATE NotifyAfterShiftSetting SET shouldNotify=:notify, remindAfter=:minutes  WHERE workplaceId =:workpalceId")
//    suspend fun setShouldNotifyOnShiftCompletion(workpalceId: Int, notify: Int, minutes: Int)


    @Query("UPDATE NotifyAfterShiftSetting SET shouldNotify=:notify WHERE workplaceId =:workpalceId")
    suspend fun setShouldNotifyOnShiftCompletion(workpalceId: Int, notify: Int)

    @Query("UPDATE NotifyAfterShiftSetting SET remindAfter=:minutes  WHERE workplaceId =:workpalceId")
    suspend fun setRemindAfterNotifyOnShiftCompletion(workpalceId: Int, minutes: Int)

    @Transaction
    suspend fun setShouldNotifyOnShiftCompletionSetting(notify: Boolean, minutes: Int) {
        setRemindAfterNotifyOnShiftCompletion(-1, minutes)
        setShouldNotifyOnShiftCompletion(-1, notify.toRoomIntValue())
    }
}