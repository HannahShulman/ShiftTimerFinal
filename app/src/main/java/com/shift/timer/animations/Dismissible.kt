package com.shift.timer.animations

//We use this to remove the Fragment only when the animation finished
interface Dismissible {
    interface OnDismissedListener {
        fun onDismissed()
    }

    fun dismiss(listener: OnDismissedListener?)
}