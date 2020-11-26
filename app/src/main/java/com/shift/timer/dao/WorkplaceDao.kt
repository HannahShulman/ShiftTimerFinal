package com.shift.timer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shift.timer.model.CurrentWorkplace
import com.shift.timer.model.Workplace
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkplaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkplace(workplace: Workplace) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWorkplace(workplace: CurrentWorkplace) : Long

    @Query("UPDATE CurrentWorkplace SET  workplaceId=:workplaceId")
    suspend fun setCurrentWorkplace(workplaceId: Int)

    @Query("SELECT * FROM workplace")
    fun getAllWorkplaces(): Flow<List<Workplace>>

    @Query("SELECT * FROM workplace WHERE workplaceId =:id")
    fun getWorkplaceById(id: Int): Flow<Workplace>
}