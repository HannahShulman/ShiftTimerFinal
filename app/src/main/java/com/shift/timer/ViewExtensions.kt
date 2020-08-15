package com.shift.timer

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

fun View.throttledClickListener(onNext: () -> Unit): Disposable {
    return clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { onNext() }
}