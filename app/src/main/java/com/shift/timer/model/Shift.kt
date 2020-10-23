package com.shift.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*
import java.util.concurrent.TimeUnit


@Entity
@TypeConverters(Converters::class)
data class Shift(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workplaceId: Int = 0,
    val start: Date,
    val end: Date?,
    val payment: Long,
    val rate: WageRatePercentage = WageRatePercentage.HUNDRED_PERCENT
) {
    val isProgress: Boolean
        get() = end != null

    val minutesInShift: Long
        get() = end?.time?.minus(start.time)?.div(1000) ?: 0
}

//returns the amount of minutes, if no ending to shift it s cauculates from start to time of calculation
fun Shift.totalTimeInMinutes(): Long {
    val startMs = start.time
    val endMs = this.end?.time ?: System.currentTimeMillis()
    val millisecondsInShift = endMs - startMs
    return TimeUnit.MILLISECONDS.toMinutes(millisecondsInShift)
}

@Entity
data class Workplace(@PrimaryKey(autoGenerate = true) val id: Int = 0, val description: String)


enum class WageRatePercentage(val value: Int) {
    HUNDRED_PERCENT(100),
    HUNDRED_TWENTY_PERCENT(120),
    HUNDRED_FIFTY_PERCENT(150),
}