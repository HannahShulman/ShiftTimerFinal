package com.shift.timer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.shift.timer.model.Shift
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

}