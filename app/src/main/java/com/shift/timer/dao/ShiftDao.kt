package com.shift.timer.dao

import androidx.room.*
import com.shift.timer.model.Shift
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShift(shift: Shift)

    @Query("SELECT * FROM shift WHERE `end` IS NULL")
    fun getCurrentShift(): Flow<Shift?>

    @Query("SELECT * FROM shift WHERE id =:shiftId")
    fun getShiftById(shiftId: Int): Flow<Shift>

    @Update
    suspend fun endShift(shift: Shift): Int

    @Query("UPDATE shift SET rate =:rate, note =:note, bonus=:bonus,  start = :start, `end` = :end  WHERE id =:id")
    suspend fun updateShift(id: Int, start: Long, end: Long?, rate: Int, note: String, bonus: Int)
}