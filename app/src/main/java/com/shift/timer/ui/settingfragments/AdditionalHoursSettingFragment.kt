package com.shift.timer.ui.settingfragments

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.minutesToHoursString
import com.shift.timer.ui.SettingSaveable
import kotlinx.android.synthetic.main.fragment_additional_hours_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AdditionalHoursSettingFragment :
    SettingBaseFragment(R.layout.fragment_additional_hours_setting_layout),
    SettingSaveable {

    var selectedPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getRegularRatePaid().collect { minutes ->
                val position = maxOf(hours_picker.data.indexOfFirst {
                    it.toString().contains(minutes.minutesToHoursString())
                }, 0)
                calculate_from.text =
                    getString(
                        R.string.calculate_after,
                        minutes.minutesToHoursString()
                    ).takeIf { position != 0 } ?: getString(R.string.dont_calculate)
                Handler().postDelayed({
                    hours_picker.setSelectedItemPosition(position, false)// = position
                    selectedPosition = position
                }, 100)
            }
        }

        hours_picker.setOnItemSelectedListener { picker, data, position ->
            selectedPosition = position
            calculate_from.text = when (position) {
                0 -> getString(R.string.dont_calculate)
                else -> {
                    val d = (data as? String)?.split(" ")
                    val total = d?.get(0)?.toDouble()?.times(60)?.toInt()
                    total?.let { getString(R.string.calculate_after, it.minutesToHoursString()) }
                        ?: getString(R.string.dont_calculate)
                }
            }
        }
    }

    override fun saveSetting() {
        val minutes =
            (((selectedPosition - 1) * 30) + 7 * 60).takeIf { selectedPosition != 0 } ?: 0
        settingsViewModel.setMinutesPaidRegularRate(minutes)
    }
}
