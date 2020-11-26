package com.shift.timer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shift.timer.model.TravelExpensesSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelExpensesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTravelExpenseSetting(setting: TravelExpensesSetting)

    //minutes above limited will be calculated with higher rates
    @Query("SELECT * FROM TravelExpensesSetting WHERE workplaceId =:workplaceId ")
    fun getTravellingExpenseSetting(workplaceId: Int): Flow<TravelExpensesSetting>

    //minutes above limited will be calculated with higher rates
    @Query("SELECT shouldCalculate FROM TravelExpensesSetting WHERE workplaceId =:workplaceId ")
    fun shouldCalculateTravelExpense(workplaceId: Int): Flow<Boolean>


    @Query("UPDATE TravelExpensesSetting SET shouldCalculate=:shouldCalculate,  singleTravelExpense=:singleExpense WHERE workplaceId =:workplaceId")
    suspend fun updateTravelExpenseSetting(
        workplaceId: Int,
        shouldCalculate: Int,
        singleExpense: Int
    )
}