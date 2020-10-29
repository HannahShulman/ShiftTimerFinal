package com.shift.timer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shift.timer.model.WageSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface WageSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWageSetting(wage: WageSetting)

    @Query("SELECT * FROM WageSetting WHERE workplaceId =:id")
    fun getWorkplaceById(id: Int): WageSetting

    @Query(
        """SELECT wage FROM WageSetting
        WHERE EXISTS(SELECT * FROM CurrentWorkplace WHERE workplaceId = WageSetting.workplaceId)"""
    )
    fun currentWorkplaceHourlyPayment(): Flow<Int>

    @Query("""UPDATE wagesetting SET  wage=:cents WHERE workplaceId =:workplaceId""")
    suspend fun setHourlyPayment(workplaceId: Int, cents: Int)

}