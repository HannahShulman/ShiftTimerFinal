package com.shift.timer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.shift.timer.R
import com.shift.timer.Setting
import com.shift.timer.ui.settingfragments.*
import kotlinx.coroutines.InternalCoroutinesApi

class SettingDetailActivity : AppCompatActivity() {
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_detail)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val setting =
            Setting.values().firstOrNull { it.name == intent.getStringExtra(SETTING_NAME) }
                ?: Setting.HOURLY_PAYMENT

        supportActionBar?.title = getString(setting.title)

        val fragment: Fragment = when (setting) {
            Setting.HOURLY_PAYMENT -> HourlyPaymentSettingFragment()
            Setting.ADDITIONAL_HOURS_CALCULATION -> AdditionalHoursSettingFragment()
            Setting.TRAVELING_EXPENSES -> TravelExpensesSettingFragment()
            Setting.BREAKS -> BreaksSettingFragment()
            Setting.MONTH_DATE_CALCULATIONS -> MonthlyCalculationCycleFragment()
//            Setting.RATE_PER_DAY -> BreaksSettingFragment()
//            Setting.NOTIFY_ARRIVAL -> BreaksSettingFragment()
//            Setting.NOTIFY_LEAVING -> BreaksSettingFragment()
//            Setting.NOTIFY_END_OF_SHIFT -> BreaksSettingFragment()
            else -> BreaksSettingFragment()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, fragment)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.save_setting -> {
                //first hide keyboard
                // Check if no view has focus:
                val view = this.currentFocus
                view?.let { v ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v.windowToken, 0)
                }
                (supportFragmentManager.fragments.firstOrNull { it is SettingSaveable } as? SettingSaveable)?.saveSetting()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val SETTING_NAME = "setting_name"
        fun start(context: Context, setting: Setting) {
            val intent = Intent(context, SettingDetailActivity::class.java)
            intent.putExtra(SETTING_NAME, setting.name)
            context.startActivity(intent)
        }
    }
}

interface SettingSaveable{
    fun settingSavedCallback()
    fun saveSetting()
}