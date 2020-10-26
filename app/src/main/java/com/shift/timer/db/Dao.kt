package com.shift.timer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.shift.timer.model.Shift
import com.shift.timer.model.TravelExpensesSetting
import com.shift.timer.model.WageSetting
import com.shift.timer.model.Workplace
import kotlinx.coroutines.flow.Flow
import java.util.*

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
    suspend fun updateTravelExpenseSetting(workplaceId: Int, shouldCalculate: Int, singleExpense: Int)
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
}