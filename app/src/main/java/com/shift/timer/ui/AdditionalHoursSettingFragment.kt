package com.shift.timer.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.viewmodels.SettingsViewModel
import com.shift.timer.viewmodels.SettingsViewModelFactory
import kotlinx.android.synthetic.main.fragment_additional_hours_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdditionalHoursSettingFragment : Fragment(R.layout.fragment_additional_hours_setting_layout),
    SettingSaveable {

    @Inject
    lateinit var factory: SettingsViewModelFactory

    private val settingsViewModel: SettingsViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getRegularRatePaid().collect { minutes ->
                calculate_from.text = getString(R.string.calculate_after, minutes.div(60.0).toString().replace(".0", ""))
                val array = resources.getStringArray(R.array.additional_hours_options)
                val position = maxOf(array.indexOfFirst { it.contains(minutes.div(60.0).toString()) }, 0)
                hours_picker.selectedItemPosition = position
            }
        }

        hours_picker.setOnItemSelectedListener { picker, data, position ->
            Log.d("TAG", "onViewCreated: $data")
            calculate_from.text = when (position) {
                0 -> getString(R.string.dont_calculate)
                else -> {
                    val d = (data as? String)?.split(" ")
                    val total = d?.get(0)?.toDouble()?.times(60)
                    total?.let { getString(R.string.calculate_after, it.div(60).toString()) }
                        ?: getString(R.string.dont_calculate)
                }
            }
        }
    }

    override fun saveSetting() {
        settingsViewModel.setHourlyPayment(100)
    }
}