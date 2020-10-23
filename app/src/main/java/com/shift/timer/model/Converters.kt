package com.shift.timer.model

import androidx.room.TypeConverter


class Converters {

    @TypeConverter
    fun fromWageRatePercentageToInt(rate: WageRatePercentage): Int = rate.value

    @TypeConverter
    fun fromIntToWageRatePercentage(rate: Int): WageRatePercentage? = WageRatePercentage.values().firstOrNull { it.value == rate }
}