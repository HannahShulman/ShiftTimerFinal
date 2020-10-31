package com.shift.timer.repositories

import com.shift.timer.SpContract
import com.shift.timer.dao.*
import com.shift.timer.model.Workplace
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkplaceRepository @Inject constructor(
    private val spContract: SpContract,
    private val workplaceDao: WorkplaceDao,
    private val wageDao: WageSettingDao,
    private val additionalHoursSettingDao: AdditionalHoursSettingDao,
    private val travelExpensesDao: TravelExpensesDao,
    private val breakCalculationsDao: BreakCalculationsDao,
    private val monthlyStartingCalculationsSettingDao: MonthlyStartingCalculationsSettingDao,
    private val ratePerDaySettingDao: RatePerDaySettingDao,
    private val notifySettingDao: NotifySettingDao
) {

    val workplaces = workplaceDao.getAllWorkplaces()

    fun selectedWorkplace(): Flow<Workplace> {
        return workplaceDao.getWorkplaceById(spContract.workplaceId)
    }

    suspend fun addWorkplace(): Long =
        coroutineScope {
            val d = async {
                workplaceDao.insertWorkplace(Workplace())
            }
            d.await()
        }
}