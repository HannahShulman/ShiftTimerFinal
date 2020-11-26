package com.shift.timer

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun View.throttledClickListener(onNext: () -> Unit): Disposable {
    return clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { onNext() }
}

fun Int.getDayOfWeekRepresentation(): String{
    return when(this){
        1-> "ראשון"
        2-> "שני"
        3-> "שלישי"
        4-> "רביעי"
        5-> "חמישי"
        6-> "שישי"
        7-> "שבת"
        else -> "".also { Throwable("Invalid day of week") }
    }
}