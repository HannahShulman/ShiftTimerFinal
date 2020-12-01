package com.shift.timer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.commit
import com.shift.timer.R
import com.shift.timer.Setting
import com.shift.timer.extensions.getFragmentBySetting
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
                ?: Setting.SALARY
        supportActionBar?.title = getString(setting.title)


        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, getFragmentBySetting(setting))
            }
        }

    }

    private fun saveChanges() {
        (supportFragmentManager.fragments.firstOrNull { it is SettingSaveable } as? SettingSaveable)?.saveSetting()
    }

    override fun onBackPressed() {
        saveChanges()
    }

    companion object {
        const val SETTING_NAME = "setting_name"
        fun start(context: Context, setting: Setting, view: View) {
            val intent = Intent(context, SettingDetailActivity::class.java)
            intent.putExtra(SETTING_NAME, setting.name)
            val pair = Pair<View, String>(view, "imageTransition")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as AppCompatActivity, pair)
            context.startActivity(intent, options.toBundle())
            (context as? AppCompatActivity)?.overridePendingTransition(
                R.anim.enter_from_bottom,
                R.anim.exit_to_bottom
            )
        }
    }
}

interface SettingSaveable {
    fun settingSavedCallback()
    fun saveSetting()
}