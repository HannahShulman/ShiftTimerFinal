package com.shift.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity
data class WageSetting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val workplaceId: Int,
    val wage: Int
)

@Entity
data class AdditionalHoursSetting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val workplaceId: Int,
    val regularRateMinutes: Int
)

@Entity
//@TypeConverters(PeriodConverter::class)
data class TravelExpensesSetting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val workplaceId: Int,
    val singleTravelExpense: Int,
//    val period: Period = Period.MONTHLY,
    val shouldCalculate: Boolean
)

//class PeriodConverter {
//    @TypeConverter
//    fun periodToString(period: Period): String = period.value
//
//    @TypeConverter
//    fun stringToPeriod(string: String): Period = Period.values().first { it.value == string }
//}

enum class Period(val value: String) {
    MONTHLY("monthly"),
    HOURLY("hourly"),

}

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