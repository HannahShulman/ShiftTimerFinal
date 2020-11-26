package com.shift.timer.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.shift.timer.R
import com.shift.timer.Setting
import com.shift.timer.SettingsListAdapter
import com.shift.timer.custom_ui.AppBarStateChangeListener
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.throttledClickListener
import com.shift.timer.ui.workplace.NoAdditionalWorkplacesDialog
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

    private val adapter: SettingsListAdapter by lazy {
        SettingsListAdapter(::onSettingSelected)
    }

    private fun onSettingSelected(setting: Setting, view: View) {
        SettingDetailActivity.start(requireContext(), setting, view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings_list.adapter = adapter

        app_bar_layout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                more_workplace_opt.isVisible = state != State.COLLAPSED
            }
        })

        collapsing_toolbar.throttledClickListener {
            val d = NoAdditionalWorkplacesDialog()
            d.show(parentFragmentManager, "")
        }

        more_workplace_opt.setOnClickListener {
            //added this listener only so this button wouldn't trigger the method on line 59 - Dan
        }

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getWorkplaceById().collect {
                collapsing_toolbar.title = it.description
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
        //notify on arrival
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.shouldNotifyOnArrival().collect {
                adapter.shouldNotifyOnLocationArrival = it
            }
        }
        //notify on leave
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.shouldNotifyOnLeave().collect {
                adapter.shouldNotifyOnLocationLeave = it
            }
        } //notify on end shift
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.shouldNotifyAfterShift().collect {
                adapter.activeRemindAfterShift = it
            }
        }
    }
}