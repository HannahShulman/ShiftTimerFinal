package com.shift.timer.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shift.timer.R
import com.shift.timer.Setting
import com.shift.timer.SettingsListAdapter
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.throttledClickListener
import com.shift.timer.viewmodels.SettingsViewModel
import com.shift.timer.viewmodels.SettingsViewModelFactory
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var factory: SettingsViewModelFactory

    private val settingsViewModel: SettingsViewModel by viewModels { factory }

    val adapter: SettingsListAdapter by lazy {
        SettingsListAdapter(::onSettingSelected)
    }

    private fun onSettingSelected(setting: Setting) {
        SettingDetailActivity.start(requireContext(), setting)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings_list.adapter = adapter

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            more_workplace_opt.isVisible = abs(verticalOffset) >= appBarLayout.totalScrollRange

        })
        workplace_title.throttledClickListener {
            BottomSheetDialogFragment().show(parentFragmentManager, "")
        }
        settingSavedCallback()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_setting -> {
                Toast.makeText(requireContext(), "ABC", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun settingSavedCallback() {}

    fun saveNotifySetting(setting: Setting, notify: Boolean) {
        when (setting) {
            Setting.NOTIFY_ARRIVAL -> settingsViewModel.notifyOnArrival(notify)
//            Setting.NOTIFY_LEAVING -> settingsViewModel.notifyOnLeave(notify)
//            Setting.NOTIFY_END_OF_SHIFT -> onSettingSelected(setting, notify)
            else -> Throwable("Should not be reaching here")
        }
    }
}