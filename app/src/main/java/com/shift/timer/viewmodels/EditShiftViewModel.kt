package com.shift.timer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shift.timer.model.Shift
import com.shift.timer.repositories.ShiftRepository
import com.shift.timer.repositories.WorkplaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class EditShiftData(
    val id: Int,
    val start: Date,
    val end: Date,
    val rate: Int,
    val bonus: Int,
    val note: String
)

class EditShiftViewModel(
    private val repository: ShiftRepository,
    val workplaceRepository: WorkplaceRepository
) : ViewModel() {

    fun getShiftById(id: Int): Flow<Shift> = repository.getShiftById(id)
    fun updateShiftData(data: EditShiftData): Boolean {
        viewModelScope.launch {
            repository.updateShiftData(
                EditShiftData(
                    id = data.id, start = data.start, end = data.end,
                    rate = data.rate, bonus = data.bonus, note = data.note
                )
            )
        }
        return true
    }
}

class EditShiftViewModelFactory @Inject constructor(
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditShiftViewModel(repository, workplaceRepository) as T
    }
}