package com.shift.timer.dao

import androidx.room.*
import com.shift.timer.extensions.toRoomIntValue
import com.shift.timer.model.NotifyAfterShiftSetting
import com.shift.timer.model.NotifyOnArrivalSetting
import com.shift.timer.model.NotifyOnLeaveSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface NotifySettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(setting: NotifyOnArrivalSetting)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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