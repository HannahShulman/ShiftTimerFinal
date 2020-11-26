package com.shift.timer

import java.text.SimpleDateFormat
import java.util.*

fun Date.dateToTimeFormat(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(this.time)