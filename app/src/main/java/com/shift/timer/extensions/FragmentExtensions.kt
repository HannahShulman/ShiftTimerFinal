package com.shift.timer.extensions

import androidx.fragment.app.Fragment

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}