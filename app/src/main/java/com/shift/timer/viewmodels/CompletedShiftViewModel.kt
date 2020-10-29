package com.shift.timer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shift.timer.model.Shift
import com.shift.timer.repositories.ShiftRepository
import com.shift.timer.repositories.WorkplaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompletedShiftViewModel(
    private val repository: ShiftRepository,
    val workplaceRepository: WorkplaceRepository
) : ViewModel() {

    fun getShiftById(id: Int): Flow<Shift> = repository.getShiftById(id)
}

class CompletedShiftViewModelFactory @Inject constructor(
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompletedShiftViewModel(repository, workplaceRepository) as T
    }
}