package com.shift.timer.repositories

import com.shift.timer.SpContract
import com.shift.timer.dao.*
import com.shift.timer.extensions.toRoomIntValue
import com.shift.timer.model.NotifyAfterShiftSetting
import com.shift.timer.model.TravelExpensesSetting
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepository @Inject constructor(
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

    val currentWorkplaceHourlyPayment: Flow<Int> = wageDao.currentWorkplaceHourlyPayment()


    fun limitMinutesToBePaidRegularRate(): Flow<Int> {
        return additionalHoursSettingDao.getLimitedMinutesForRegularPayment(spContract.workplaceId)
    }

    fun shouldCalculateTravelExpenses(): Flow<Boolean> {
        return travelExpensesDao.shouldCalculateTravelExpense(spContract.workplaceId)
    }

    fun breakMinutesToDeduct(): Flow<Int> {
        return breakCalculationsDao.breakMinutesToDeduct(spContract.workplaceId)
    }

    fun dayStartCalculations(): Flow<Int> {
        return monthlyStartingCalculationsSettingDao.dayStartingCalculation(spContract.workplaceId)
    }

    suspend fun setHourlyPayment(cents: Int) {
        wageDao.setHourlyPayment(spContract.workplaceId, cents)
    }

    suspend fun setMinutesPaidRegularRate(cents: Int) {
        additionalHoursSettingDao.setMinutesPaidRegularRate(spContract.workplaceId, cents)
    }

    suspend fun updateTravelExpenseSetting(shouldCalculate: Boolean, singleTravelExpense: Int) {
        travelExpensesDao.updateTravelExpenseSetting(
            spContract.workplaceId,
            1.takeIf { shouldCalculate } ?: 0,
            singleTravelExpense
        )
    }

    suspend fun updateRatePerDaySetting(rates: List<DayOfWeekWithRate>) {
        ratePerDaySettingDao.updateRatePerDay(rates)
    }

    suspend fun notifyOnArrival(notify: Boolean) {
        notifySettingDao.updateNotifyOnArrive(spContract.workplaceId, notify.toRoomIntValue())
    }

    suspend fun notifyOnLeave(notify: Boolean) {
        notifySettingDao.updateNotifyOnLeave(spContract.workplaceId, notify.toRoomIntValue())
    }

    fun shouldNotifyOnArrival(): Flow<Boolean> {
        return notifySettingDao.notifyOnArrive(spContract.workplaceId)
    }

    fun shouldNotifyOnLeave(): Flow<Boolean> {
        return notifySettingDao.notifyOnLeave(spContract.workplaceId)
    }

    fun shouldNotifyAfterShift(): Flow<Boolean> {
        return notifySettingDao.shouldNotifyOnShiftCompletion(spContract.workplaceId)
    }

    fun getTravellingExpenseSetting(): Flow<TravelExpensesSetting> {
        return travelExpensesDao.getTravellingExpenseSetting(spContract.workplaceId)
    }

    fun getBreakMinutesToCalculate(): Flow<Int> {
        return breakCalculationsDao.breakMinutesToDeduct(spContract.workplaceId)
    }

    fun getDayOfMonthlyCycle(): Flow<Int> {
        return monthlyStartingCalculationsSettingDao.dayStartingCalculation(spContract.workplaceId)
    }

    fun getNotifyAfterShiftSetting(): Flow<NotifyAfterShiftSetting> {
        return notifySettingDao.getNotifyOnShiftCompletionSetting(spContract.workplaceId)
    }

    suspend fun setBreakMinutesToDeduct(minutesToDeduct: Int) {
        breakCalculationsDao.setBreakMinutesToDeduct(spContract.workplaceId, minutesToDeduct)
    }

    suspend fun startMonthlyCalculationCycle(dayOfMonth: Int) {
        monthlyStartingCalculationsSettingDao.startMonthlyCalculationCycle(
            spContract.workplaceId,
            dayOfMonth
        )
    }

    suspend fun setShouldNotifyOnShiftCompletionSetting(notify: Boolean, minutes: Int) {
        notifySettingDao.setShouldNotifyOnShiftCompletionSetting(notify, minutes)
    }
}