package com.shift.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.shift.timer.Setting
import com.shift.timer.viewmodels.EditShiftData
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
@TypeConverters(Converters::class)
data class Shift(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workplaceId: Int = 0,
    val start: Date,
    val end: Date?,
    val payment: Long//cents per hour, calculation should be upon display
) {
    val isProgress: Boolean
        get() = end != null

    var rate: WageRatePercentage = WageRatePercentage.HUNDRED_PERCENT
    var note: String = ""
    var bonus: Int = 0

    val getPaymentDisplay: String
        get() {
            val payInCents = (payment.div(60.00).toLong()).times(totalTimeInMinutes())
            return String.format(Locale.getDefault(), "%.2f", payInCents.div(100.0).toFloat())
        }

    val minutesInShift: Long
        get() = end?.time?.minus(start.time)?.div(1000) ?: 0
}

fun Shift.getUpdatedShift(data: EditShiftData): Shift {
    return apply {
        rate = WageRatePercentage.values().firstOrNull { it.value == data.rate }
            ?: WageRatePercentage.HUNDRED_PERCENT
    }

}

//returns the amount of minutes, if no ending to shift it s cauculates from start to time of calculation
fun Shift.totalTimeInMinutes(): Long {
    val startMs = start.time
    val endMs = this.end?.time ?: System.currentTimeMillis()
    val millisecondsInShift = endMs - startMs
    return TimeUnit.MILLISECONDS.toMinutes(millisecondsInShift)
}

@Entity
data class Workplace(
    @PrimaryKey(autoGenerate = true) val workplaceId: Int = 0,
    val description: String = ""
)

data class WorkplaceWithSettings(val workplaceId: Int, val settings: List<Setting>)


enum class WageRatePercentage(val value: Int) {
    HUNDRED_PERCENT(100),
    HUNDRED_TWENTY__FIVE_PERCENT(125),
    HUNDRED_FIFTY_PERCENT(150),
    TWO_HUNDRED(200),
}