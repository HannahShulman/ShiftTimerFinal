package com.shift.timer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shift.timer.model.Shift
import com.shift.timer.model.Workplace
import com.shift.timer.repositories.ShiftRepository
import com.shift.timer.repositories.WorkplaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class CurrentShiftViewModel(
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository
) : ViewModel() {

    suspend fun enterShift() {
        repository.startShift()
    }

    suspend fun endShift(shift: Shift) {
        repository.endShift(shift)
    }

    val currentShift = repository.getCurrentShift

    private val workplaces = workplaceRepository.workplaces

    val hasMultipleWorkplaces: Flow<Boolean> = workplaces.mapLatest { it.size > 1 }

    val shiftLengthInSeconds = MutableStateFlow(9.times(60).times(60))

    val selectedWorkplace: Flow<Workplace> = workplaceRepository.selectedWorkplace()

    fun getShiftById(id: Int): Flow<Shift> {
        return repository.getShiftById(id)
    }
}

class CurrentShiftViewModelFactory @Inject constructor(
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentShiftViewModel(repository, workplaceRepository) as T
    }
}