package com.shift.timer

import java.text.SimpleDateFormat
import java.util.*

fun Long.timeRemainingText(shiftLengthInSeconds: Long): String{
    return "Display remaining text"
}

fun Date.dateToTimeFormat(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(this.time)