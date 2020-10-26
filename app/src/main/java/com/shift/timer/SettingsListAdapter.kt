package com.shift.timer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SettingsListAdapter(val settingClickListener:(setting: Setting)-> Unit) : RecyclerView.Adapter<SettingsListAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val icon = itemView.findViewById<ImageView>(R.id.icon)
        val value = itemView.findViewById<TextView>(R.id.value)

        init {

        }
    }

    val data: Array<Setting>
        get() = Setting.values()

    var hourlyPayment: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var startHigherRatePaymentFrom: Double = 0.0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var calculateTravelExpenses: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var minutesToDeduct: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var startDayCalculation: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_setting_item_layout.takeIf { viewType == 0 }
                    ?: R.layout.single_setting_header_item_layout, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].type != SettingType.HEADER) 0 else 1
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.title.text = holder.itemView.context.getString(data[position].title)
        if (data[position].type == SettingType.HEADER) return
        holder.itemView.setOnClickListener { settingClickListener(data[position]) }
        holder.icon.setImageResource(data[position].icon)
        holder.value.text = when (data[position]) {
            Setting.HOURLY_PAYMENT -> context.getString(R.string.total_payment, hourlyPayment)
            Setting.ADDITIONAL_HOURS_CALCULATION -> startHigherRatePaymentFrom.toString()
                .takeIf { startHigherRatePaymentFrom > 0 }
                ?: context.getString(R.string.dont_calculate)
            Setting.TRAVELING_EXPENSES -> context.getString(R.string.calculate.takeIf { calculateTravelExpenses }
                ?: R.string.dont_calculate)
            Setting.BREAKS -> context.getString(R.string.total_time, minutesToDeduct.toString()).takeIf { minutesToDeduct > 0 }
                ?: context.getString(R.string.dont_calculate)
//            Setting.MONTH_DATE_CALCULATIONS -> when (startDayCalculation) {
//                1 -> context.getString(R.string.payment_cycle, startDayCalculation, 30)
//                else -> context.getString(R.string.payment_cycle, startDayCalculation, startDayCalculation - 1)
//            }
//
//            Setting.RATE_PER_DAY -> ""
//            Setting.NOTIFY_ARRIVAL -> ""
//            Setting.NOTIFY_LEAVING -> ""
//            Setting.NOTIFY_END_OF_SHIFT -> ""
            Setting.PAYMENTS_HEADER -> ""
//            Setting.NOTIFY_HEADER -> ""
        }
    }
}

enum class SettingType {
    PAYMENTS, NOTIFICATION, HEADER
}

enum class Setting(val title: Int, val icon: Int, val type: SettingType) {
    PAYMENTS_HEADER(
        R.string.salary,
        R.drawable.hourly_rate,
        SettingType.HEADER
    ),
    HOURLY_PAYMENT(
        R.string.hourly_payment,
        R.drawable.hourly_rate,
        SettingType.PAYMENTS
    ),
    ADDITIONAL_HOURS_CALCULATION(
        R.string.additional_hours,
        R.drawable.additional_hours,
        SettingType.PAYMENTS
    ),
    TRAVELING_EXPENSES(
        R.string.travel_expense,
        R.drawable.ic_travelling_expenses,
        SettingType.PAYMENTS
    ),
    BREAKS(R.string.breaks, R.drawable.ic_breaks, SettingType.PAYMENTS),
//    MONTH_DATE_CALCULATIONS(
//        R.string.calculation_period,
//        R.drawable.ic_cycle_calculation,
//        SettingType.PAYMENTS
//    ),
//    RATE_PER_DAY(R.string.rate_per_day, R.drawable.ic_special_rate, SettingType.PAYMENTS),
//    NOTIFY_HEADER(
//        R.string.reminders,
//        R.drawable.hourly_rate,
//        SettingType.HEADER
//    ),
//    NOTIFY_ARRIVAL(
//        R.string.notify_arrival,
//        R.drawable.check_in_shift_icon,
//        SettingType.NOTIFICATION
//    ),
//    NOTIFY_LEAVING(
//        R.string.notify_exit,
//        R.drawable.check_out_shift_icon,
//        SettingType.NOTIFICATION
//    ),
//    NOTIFY_END_OF_SHIFT(
//        R.string.notify_end_of_shift,
//        R.drawable.ic_reminder_on_shift_completion,
//        SettingType.NOTIFICATION
//    ),
}