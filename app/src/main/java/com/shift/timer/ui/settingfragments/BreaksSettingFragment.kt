package com.shift.timer.ui.settingfragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.ui.SettingSaveable
import kotlinx.android.synthetic.main.fragment_additional_hours_setting_layout.calculate_from
import kotlinx.android.synthetic.main.fragment_breaks_calculation_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class BreaksSettingFragment :
    SettingBaseFragment(R.layout.fragment_breaks_calculation_setting_layout),
    SettingSaveable {

    var positionSelected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        minutes_picker.selectedItemPosition = 0

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getBreakMinutesToCalculate().collect { minutes ->
                calculate_from.text = getString(R.string.calculate_break_after, minutes.toString())
                val array = resources.getStringArray(R.array.break_calculations_options)
                val position = maxOf(array.indexOfFirst { it == minutes.toString() }, 0)
                minutes_picker.setSelectedItemPosition(position, true)
                minutes_picker.setSelectedItemPosition(position, true)
                positionSelected = minutes_picker.selectedItemPosition
            }
        }

        minutes_picker.setOnItemSelectedListener { picker, data, position ->
            positionSelected = position
            calculate_from.text = when (position) {
                0 -> getString(R.string.dont_calculate)
                else -> {
                    getString(R.string.calculate_break_after, data.toString())
                }
            }
        }
    }

    override fun saveSetting() {
        settingsViewModel.setBreakMinutesToDeduct(positionSelected * 5)
    }
}