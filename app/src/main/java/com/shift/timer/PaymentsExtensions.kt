package com.shift.timer

import java.util.*

fun Int.centsToPaymentFormat(): String =
    String.format(Locale.getDefault(), "%.2f", div(100.0).toFloat())

fun String.inputToCents(): Int {
    val sb = split(".")
    return if (sb.isNotEmpty()) {
        val dollarsToCents = sb[0].toInt() * 100
        val cents = if (sb.size > 1) sb[1].toInt() else 0
        dollarsToCents + cents
    } else 0
}