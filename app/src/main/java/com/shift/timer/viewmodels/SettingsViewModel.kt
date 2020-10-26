package com.shift.timer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shift.timer.SpContract
import com.shift.timer.db.*
import com.shift.timer.model.TravelExpensesSetting
import com.shift.timer.model.Workplace
import com.shift.timer.ui.ShiftRepository
import com.shift.timer.ui.WorkplaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel
    (
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settingSaved = MutableLiveData(false)

    fun getWorkplaceById(): Flow<Workplace> = workplaceRepository.selectedWorkplace()

    fun getHourlyPayment(): Flow<Int> = settingsRepository.getHourlyPaymentBySelectedId()

    fun getRegularRatePaid(): Flow<Int> = settingsRepository.limitMinutesToBePaidRegularRate()
    fun getBreakMinutesToCalculate(): Flow<Int> = settingsRepository.getBreakMinutesToCalculate()

    fun shouldCalculateTravelExpenses(): Flow<Boolean> =
        settingsRepository.shouldCalculateTravelExpenses()

    fun minutesToDeduct(): Flow<Int> =
        settingsRepository.breakMinutesToDeduct()

    fun dayStartCalculations(): Flow<Int> =
        settingsRepository.dayStartCalculations()

    fun setHourlyPayment(cents: Int) {
        val c = viewModelScope.launch {
            settingsRepository.setHourlyPayment(cents)
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }
    fun setBreakMinutesToDeduct(minutes: Int) {
        val c = viewModelScope.launch {
            settingsRepository.setBreakMinutesToDeduct(minutes)
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }

    fun getTravellingExpenseSetting(): Flow<TravelExpensesSetting> {
        return settingsRepository.getTravellingExpenseSetting()
    }

    fun updateTravelExpenseSetting(shouldCalculate: Boolean, singleTravelExpense: Int) {
        val c = viewModelScope.launch {
            settingsRepository.updateTravelExpenseSetting(shouldCalculate, singleTravelExpense)
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }
}

class SettingsViewModelFactory @Inject constructor(
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository, workplaceRepository, settingsRepository) as T
    }
}

class SettingsRepository @Inject constructor(
    private val spContract: SpContract,
    private val workplaceDao: WorkplaceDao,
    private val wageDao: WageSettingDao,
    private val additionalHoursSettingDao: AdditionalHoursSettingDao,
    private val travelExpensesDao: TravelExpensesDao,
    private val breakCalculationsDao: BreakCalculationsDao,
    private val monthlyStartingCalculationsSettingDao: MonthlyStartingCalculationsSettingDao
) {

    val workplaces = workplaceDao.getAllWorkplaces()

    fun getHourlyPaymentBySelectedId(): Flow<Int> {
        return wageDao.getHourlyPayment(spContract.workplaceId)
    }

    fun limitMinutesToBePaidRegularRate(): Flow<Int> {
        return additionalHoursSettingDao.getLimitedMinutesForRegularPayment(-1)//spContract.workplaceId)
    }

    fun shouldCalculateTravelExpenses(): Flow<Boolean> {
        return travelExpensesDao.shouldCalculateTravelExpense(-1)//spContract.workplaceId)
    }

    fun breakMinutesToDeduct(): Flow<Int> {
        return breakCalculationsDao.breakMinutesToDeduct(-1)//spContract.workplaceId)
    }

    fun dayStartCalculations(): Flow<Int> {
        return monthlyStartingCalculationsSettingDao.dayStartingCalculation(-1)//spContract.workplaceId)
    }

    suspend fun setHourlyPayment(cents: Int) {
        wageDao.setHourlyPayment(-1, cents)
    }

    suspend fun updateTravelExpenseSetting(shouldCalculate: Boolean, singleTravelExpense: Int) {
        travelExpensesDao.updateTravelExpenseSetting(
            -1,
            1.takeIf { shouldCalculate } ?: 0,
            singleTravelExpense
        )
    }

    fun getTravellingExpenseSetting(): Flow<TravelExpensesSetting> {
        return travelExpensesDao.getTravellingExpenseSetting(-1)
    }

    fun getBreakMinutesToCalculate() : Flow<Int>{
        return breakCalculationsDao.breakMinutesToDeduct(-1)
    }

    suspend fun setBreakMinutesToDeduct(minutesToDeduct: Int){
        breakCalculationsDao.setBreakMinutesToDeduct(-1, minutesToDeduct)
    }
}