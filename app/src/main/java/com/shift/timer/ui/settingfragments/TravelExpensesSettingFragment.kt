package com.shift.timer.ui.settingfragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.centsToPaymentFormat
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.inputToCents
import com.shift.timer.ui.SettingSaveable
import kotlinx.android.synthetic.main.fragment_travel_expenses_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TravelExpensesSettingFragment :
    SettingBaseFragment(R.layout.fragment_travel_expenses_setting_layout),
    SettingSaveable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getTravellingExpenseSetting().collect { setting ->
                calculate_toggle.isChecked = setting.shouldCalculate
                calculate_toggle.requestFocus()
                payment.setText(setting.singleTravelExpense.centsToPaymentFormat())
                payment.setSelection(payment.text.toString().length)
                cycle_segments.check(R.id.per_month)
            }
        }
    }

    override fun saveSetting() {
        settingsViewModel.updateTravelExpenseSetting(
            calculate_toggle.isChecked,
            payment.text.toString().inputToCents()
        )
    }
}