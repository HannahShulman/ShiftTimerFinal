package com.shift.timer.ui.settingfragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.centsToPaymentFormat
import com.shift.timer.databinding.FragmentPaymentSettingLayoutBinding
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.inputToCents
import com.shift.timer.ui.SettingSaveable
import com.shift.timer.viewBinding
import kotlinx.android.synthetic.main.fragment_payment_setting_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HourlyPaymentSettingFragment : SettingBaseFragment(R.layout.fragment_payment_setting_layout),
    SettingSaveable {

    val binding: FragmentPaymentSettingLayoutBinding by viewBinding (FragmentPaymentSettingLayoutBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = getString(R.string.salary)

        binding.closeFragment.setOnClickListener {
            super.saveChangesAndCloseActivity()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.getHourlyPayment().collect {
                payment.setText(it.centsToPaymentFormat())
            }
        }
    }

    override fun saveSetting() {
        if (binding.payment.text.isEmpty()) {
            Toast.makeText(context, R.string.alert_no_parameter_entered, Toast.LENGTH_SHORT).show()
            return
        }
        settingsViewModel.setHourlyPayment(payment.text.toString().inputToCents())
    }
}