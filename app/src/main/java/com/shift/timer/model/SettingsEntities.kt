package com.shift.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrentWorkplace(@PrimaryKey val workplaceId: Int = -1)

@Entity
data class WageSetting(
    @PrimaryKey val workplaceId: Int,
    val wage: Int
)

@Entity
data class AdditionalHoursSetting(
    @PrimaryKey val workplaceId: Int,
    val regularRateMinutes: Int
)

@Entity
data class TravelExpensesSetting(
    @PrimaryKey val workplaceId: Int,
    val singleTravelExpense: Int,
    val shouldCalculate: Boolean
)

@Entity
data class BreakCalculationsSetting(
    @PrimaryKey val workplaceId: Int,
    val minutesToDeduct: Int
)

@Entity
data class MonthlyStartingCalculationsSetting(
    @PrimaryKey val workplaceId: Int,
    val dayOfMonth: Int//the day of month calculation begins
)

@Entity(primaryKeys = ["workplaceId", "dayOfWeek"])
data class RatePerDaySetting(
    val workplaceId: Int,
    val dayOfWeek: Int,
    val rate: Int = 100
)

@Entity
data class NotifyOnArrivalSetting(
    @PrimaryKey val workplaceId: Int,
    val shouldNotify: Boolean
)

@Entity
data class NotifyOnLeaveSetting(
    @PrimaryKey val workplaceId: Int,
    val shouldNotify: Boolean
)

@Entity
data class NotifyAfterShiftSetting(
    @PrimaryKey val workplaceId: Int,
    val shouldNotify: Boolean,
    val remindAfter: Int//after how many minutes to notify user.
)