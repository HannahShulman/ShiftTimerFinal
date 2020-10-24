package com.shift.timer.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.Setting
import com.shift.timer.SettingsListAdapter
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.viewmodels.SettingsViewModel
import com.shift.timer.viewmodels.SettingsViewModelFactory
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var factory: SettingsViewModelFactory

    private val settingsViewModel: SettingsViewModel by viewModels { factory }

    val adapter: SettingsListAdapter by lazy {
        SettingsListAdapter(::onSettingSelected)
    }

    private fun onSettingSelected(setting: Setting) {
//        Toast.makeText(context, setting.name, Toast.LENGTH_SHORT).show()
//        activity?.startActivity(Intent(requireContext(), SettingDetailActivity::class.java))
        SettingDetailActivity.start(requireContext(), setting)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings_list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getWorkplaceById().collect {
                workplace_title.text = it.description
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getHourlyPayment().collect {
                adapter.hourlyPayment =
                    String.format(Locale.getDefault(), "%.2f", it.div(100.0).toFloat())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getRegularRatePaid().collect {
                adapter.startHigherRatePaymentFrom = (it.div(60.0))
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.shouldCalculateTravelExpenses().collect {
                adapter.calculateTravelExpenses = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.minutesToDeduct().collect {
                adapter.minutesToDeduct = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.dayStartCalculations().collect {
                adapter.startDayCalculation = it
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_setting -> {
                Toast.makeText(requireContext(), "ABC", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}