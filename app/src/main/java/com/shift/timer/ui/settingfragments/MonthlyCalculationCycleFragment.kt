package com.shift.timer.ui.settingfragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.ui.SettingSaveable
import kotlinx.android.synthetic.main.fragment_monthly_cycle_calculation_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MonthlyCalculationCycleFragment :
    SettingBaseFragment(R.layout.fragment_monthly_cycle_calculation_setting_layout),
    SettingSaveable {

    var positionSelected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cycle_range_picker.selectedItemPosition = 0

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getDayOfMonthlyCycle().collect { dayOfMonth ->
                calculate_from.text = getString(R.string.calculate_full_month).takeIf {
                    dayOfMonth == 1
                } ?: getString(R.string.calculate_cycle_from_to, dayOfMonth, dayOfMonth - 1)
                cycle_range_picker.selectedItemPosition = dayOfMonth - 1
                positionSelected = cycle_range_picker.selectedItemPosition
            }
        }

        cycle_range_picker.setOnItemSelectedListener { picker, data, position ->
            positionSelected = position
            calculate_from.text = getString(R.string.calculate_full_month).takeIf {
                position == 0
            } ?: getString(R.string.calculate_cycle_from_to, position + 1, position)
        }
    }

    override fun saveSetting() {
        settingsViewModel.startMonthyCalculationCycle(positionSelected + 1)
    }
}