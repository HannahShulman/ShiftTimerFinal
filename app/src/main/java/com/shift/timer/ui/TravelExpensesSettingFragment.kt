package com.shift.timer.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.shift.timer.R
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.viewmodels.SettingsViewModel
import com.shift.timer.viewmodels.SettingsViewModelFactory
import kotlinx.android.synthetic.main.fragment_travel_expenses_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TravelExpensesSettingFragment : Fragment(R.layout.fragment_travel_expenses_setting_layout),
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

        settingsViewModel.settingSaved.observe(viewLifecycleOwner, Observer {
            if (it)
                Snackbar.make(view, "השינויים נשמרו בהצלחה", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.banner_blue)).show()
        })

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getTravellingExpenseSetting().collect { setting ->
                calculate_toggle.isChecked = setting.shouldCalculate
                calculate_toggle.requestFocus()
                payment.setText(
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        setting.singleTravelExpense.div(100.0).toFloat()
                    )
                )
                payment.setSelection(payment.text.toString().length)
                cycle_segments.check(R.id.per_month)
            }
        }
    }

    override fun saveSetting() {
        val sb = payment.text.toString().split(".")
        val newValue = if (sb.isNotEmpty()) {
            val dollarsToCents = sb[0].toInt() * 100
            val cents = if (sb.size > 1) sb[1].toInt() else 0
            dollarsToCents + cents
        } else 0
        settingsViewModel.updateTravelExpenseSetting(calculate_toggle.isChecked, newValue)
    }
}