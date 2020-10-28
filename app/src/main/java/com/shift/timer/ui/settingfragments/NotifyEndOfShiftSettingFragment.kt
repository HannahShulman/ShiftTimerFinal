package com.shift.timer.ui.settingfragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.ui.SettingSaveable
import kotlinx.android.synthetic.main.fragment_notify_after_shift_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotifyEndOfShiftSettingFragment :
    SettingBaseFragment(R.layout.fragment_notify_after_shift_setting_layout),
    SettingSaveable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    private fun setText() {
        val hours = hours_picker.currentHour.takeUnless { it == 0 }?: 12
        val minutes = minutes_picker.currentMinute
        calculate_from.text = if (minutes == 0) {
            requireContext().getString(R.string.remind_me_time_after_shift_begins, hours)
        } else {
            requireContext().getString(
                R.string.remind_me_time_after_shift_begins_with_minutes,
                hours,
                minutes
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hours_picker.setIsAmPm(true)
        minutes_picker.setStepSizeMinutes(1)


        hours_picker.setHourChangedListener { picker, hour ->
            setText()
        }
        minutes_picker.setOnMinuteChangedListener { picker, minute ->
            setText()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getNotifyAfterShiftSetting().collect { setting ->
                val hours = setting.remindAfter.div(60)
                val minutes = setting.remindAfter % 60
                val hoursString = "0${hours}".takeIf { hours < 10 } ?: hours.toString()
                val minutesString = "0${minutes}".takeIf { minutes < 10 } ?: minutes.toString()
                Log.d("TAG", "onViewCreated: $hoursString  $minutesString")
                hours_picker.setDefault(hoursString)
                minutes_picker.setDefault(minutesString)
                setText()
            }
        }
    }

    override fun saveSetting() {
        val hours = hours_picker.currentHour.takeIf { it != 0 } ?: 12
        val minutes = minutes_picker.currentMinute
        val total = hours.times(60).plus(minutes)
        settingsViewModel.setShouldNotifyOnShiftCompletionSetting(remind_toggle.isChecked, total)
    }
}