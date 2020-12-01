package com.shift.timer.ui.settingfragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.shift.timer.R
import com.shift.timer.ui.SettingDetailActivity
import com.shift.timer.ui.SettingSaveable
import com.shift.timer.viewmodels.SettingsViewModel
import com.shift.timer.viewmodels.SettingsViewModelFactory
import javax.inject.Inject

abstract class SettingBaseFragment(layout: Int) : Fragment(layout),
    SettingSaveable {

    @Inject
    lateinit var factory: SettingsViewModelFactory

    val settingsViewModel: SettingsViewModel by viewModels { factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingSavedCallback()
    }

    override fun settingSavedCallback() {
        settingsViewModel.settingSaved.observe(viewLifecycleOwner, Observer {
            if (it) {
                view?.let {
                    Toast.makeText(context, "השינויים נשמרו בהצלחה", Toast.LENGTH_SHORT).show()
                    (context as? SettingDetailActivity)?.finish()
                }
            }
        })
    }

    fun saveChangesAndCloseActivity() {
        (context as? SettingDetailActivity)?.onBackPressed()
    }
}