package com.shift.timer.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.shift.timer.BuildConfig
import com.shift.timer.R

const val disableAnimations = true

val enter = R.anim.enter_from_right.takeUnless { disableAnimations } ?: 0
val exit = R.anim.exit_to_left.takeUnless { disableAnimations } ?: 0
val popEnter = R.anim.enter_from_left.takeUnless { disableAnimations } ?: 0
val popExit = R.anim.exit_to_right.takeUnless { disableAnimations } ?: 0



fun AppCompatActivity.replaceFragmentInActivity(fm: FragmentManager,
                              fragment: Fragment,
                              containerId: Int,
                              addToStack: Boolean,
                              addAnimation: Boolean = false) {

    val transaction = fm.beginTransaction()
    if (addToStack) {
        transaction.addToBackStack("frag")
    }

    if (addAnimation) {
        transaction.setCustomAnimations(enter, exit, popEnter, popExit)
    }
    transaction.replace(containerId, fragment)
    transaction.commit()
}