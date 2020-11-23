package com.shift.timer.repositories

import com.shift.timer.SpContract
import com.shift.timer.dao.ShiftDao
import com.shift.timer.dao.WageSettingDao
import com.shift.timer.model.Shift
import com.shift.timer.viewmodels.EditShiftData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ShiftRepository @Inject constructor(
    private val spContract: SpContract,
    private val shiftDao: ShiftDao,
    private val wageSettingDao: WageSettingDao
) {

    suspend fun startShift() {
        GlobalScope.launch {
            val wage = wageSettingDao.getWorkplaceById(spContract.workplaceId)
            val paymentForHourShift = wage.wage//per hour cents
            shiftDao.insertShift(
                Shift(
                    workplaceId = spContract.workplaceId,
                    start = Date(),
                    end = null,
                    payment = paymentForHourShift.toLong()
                )
            )
        }
    }

    suspend fun endShift(shift: Shift) {
        val endTime = Date()
        GlobalScope.launch {
            shift.copy(end = endTime).also {
                shiftDao.endShift(it)
            }
        }
    }

    fun getShiftById(id: Int): Flow<Shift> {
        return shiftDao.getShiftById(id)
    }

    suspend fun updateShiftData(data: EditShiftData) {
        return shiftDao.updateShift(
            data.id,
            data.start.time,
            data.end.time,
            data.rate,
            data.note,
            data.bonus
        )
    }

    val getCurrentShift = shiftDao.getCurrentShift()
}