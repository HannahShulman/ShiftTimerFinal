package com.shift.timer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shift.timer.dao.DayOfWeekWithRate
import com.shift.timer.model.NotifyAfterShiftSetting
import com.shift.timer.model.TravelExpensesSetting
import com.shift.timer.model.Workplace
import com.shift.timer.repositories.SettingsRepository
import com.shift.timer.repositories.ShiftRepository
import com.shift.timer.repositories.WorkplaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel
    (
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val id = workplaceRepository.addWorkplace()
        }
    }

    val settingSaved = MutableLiveData(false)

    fun getWorkplaceById(): Flow<Workplace> = workplaceRepository.selectedWorkplace()

    fun getHourlyPayment(): Flow<Int> = settingsRepository.currentWorkplaceHourlyPayment

    fun getRegularRatePaid(): Flow<Int> = settingsRepository.limitMinutesToBePaidRegularRate()
    fun getBreakMinutesToCalculate(): Flow<Int> = settingsRepository.getBreakMinutesToCalculate()
    fun getDayOfMonthlyCycle(): Flow<Int> = settingsRepository.getDayOfMonthlyCycle()

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

    fun setMinutesPaidRegularRate(minutes: Int) {
        val c = viewModelScope.launch {
            settingsRepository.setMinutesPaidRegularRate(minutes)
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

    fun startMonthyCalculationCycle(dayOfMonth: Int) {
        val c = viewModelScope.launch {
            settingsRepository.startMonthlyCalculationCycle(dayOfMonth)
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }

    fun setShouldNotifyOnShiftCompletionSetting(notify: Boolean, minutes: Int) {
        val c = viewModelScope.launch {
            settingsRepository.setShouldNotifyOnShiftCompletionSetting(notify, minutes)
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }

    fun getTravellingExpenseSetting(): Flow<TravelExpensesSetting> {
        return settingsRepository.getTravellingExpenseSetting()
    }

    fun shouldNotifyOnArrival(): Flow<Boolean> {
        return settingsRepository.shouldNotifyOnArrival()
    }

    fun shouldNotifyOnLeave(): Flow<Boolean> {
        return settingsRepository.shouldNotifyOnLeave()
    }

    fun shouldNotifyAfterShift(): Flow<Boolean> {
        return settingsRepository.shouldNotifyAfterShift()
    }

    fun getNotifyAfterShiftSetting(): Flow<NotifyAfterShiftSetting> {
        return settingsRepository.getNotifyAfterShiftSetting()
    }

    fun updateTravelExpenseSetting(shouldCalculate: Boolean, singleTravelExpense: Int) {
        val c = viewModelScope.launch {
            settingsRepository.updateTravelExpenseSetting(shouldCalculate, singleTravelExpense)
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }

    fun updateRatePerDateSetting(rates: List<DayOfWeekWithRate>) {
        val c = viewModelScope.launch {
            settingsRepository.updateRatePerDaySetting(rates)

            setOf<Int>().count()
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }

    fun notifyOnArrival(notify: Boolean) {
        val c = viewModelScope.launch {
            settingsRepository.notifyOnArrival(notify)

            setOf<Int>().count()
        }

        c.invokeOnCompletion {
            settingSaved.value = true
        }
    }

    fun notifyOnLeave(notify: Boolean) {
        viewModelScope.launch {
            settingsRepository.notifyOnLeave(notify)

            setOf<Int>().count()
        }.invokeOnCompletion {
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