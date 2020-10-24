package com.shift.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WageSetting(@PrimaryKey(autoGenerate = true) val id: Int, val workplaceId: Int, val wage: Int)

@Entity
data class AdditionalHoursSetting(@PrimaryKey(autoGenerate = true) val id: Int, val workplaceId: Int, val regularRateMinutes: Int)

@Entity
data class TravelExpensesSetting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val workplaceId: Int,
    val singleTravelExpense: Int,
    val shouldCalculate: Boolean
)

@Entity
data class BreakCalculationsSetting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val workplaceId: Int,
    val minutesToDeduct: Int
)

@Entity
data class MonthlyStartingCalculationsSetting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val workplaceId: Int,
    val dayOfMonth: Int//the day of month calculation begins
)