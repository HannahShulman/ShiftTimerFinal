package com.shift.timer

import com.shift.timer.model.Shift

object ShiftPaymentCalculation {
    fun getShiftPayment(shift: Shift): Double {
        return shift.rate.value.times(shift.minutesInShift).toDouble()
    }
}