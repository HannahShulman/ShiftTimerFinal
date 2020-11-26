package com.shift.timer

import java.util.*

fun Int.centsToPaymentFormat(): String =
    String.format(Locale.getDefault(), "%.2f", div(100.0).toFloat())

fun String.inputToCents(): Int {
    val sb = split(".")
    return if (sb.isNotEmpty()) {
        val dollarsToCents = sb[0].toInt() * 100
        val cents = if (sb.size > 1) {
            if (sb[1].length == 1) {
                sb[1].toInt().times(10)
            } else {
                sb[1].substring(0, 2).toInt()
            }
        } else 0
        dollarsToCents + cents
    } else 0
}

fun String.removeTrailingZero(): String{
    if(contains(".00")){
       return replace(".00", "")
    } else if(contains(".0")) {
        return replace(".0", "")
    }
    return this
}

fun Int.minutesToHours():Double{
    return div(60.0)
}

fun Int.minutesToHoursString(): String{
    return minutesToHours().toString().removeTrailingZero()
}