package com.shift.timer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SettingsListAdapter(val settingClickListener: (setting: Setting, view: View) -> Unit) :
    RecyclerView.Adapter<SettingsListAdapter.SettingViewHolder>() {

    class SettingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val value = itemView.findViewById<TextView>(R.id.value)
        val title = itemView.findViewById<TextView>(R.id.title)
        val icon = itemView.findViewById<ImageView>(R.id.icon)
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

    var shouldNotifyOnLocationArrival: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var shouldNotifyOnLocationLeave: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var activeRemindAfterShift: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SettingViewHolder(
            inflater.inflate(
                R.layout.single_setting_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.title.text = holder.itemView.context.getString(data[position].title)
        holder.itemView.setOnClickListener {
            settingClickListener(data[position], holder.itemView)
        }
        holder.icon.setImageResource(data[position].icon)
        holder.value.text = when (data[position]) {
            Setting.SALARY -> context.getString(R.string.total_payment, hourlyPayment)
            Setting.ADDITIONAL_HOURS_CALCULATION -> startHigherRatePaymentFrom.toString()
                .removeTrailingZero()
                .takeIf { startHigherRatePaymentFrom > 0 }
                ?: context.getString(R.string.dont_calculate)
            Setting.TRAVELING_EXPENSES -> context.getString(R.string.calculate.takeIf { calculateTravelExpenses }
                ?: R.string.dont_calculate)
            Setting.BREAKS -> context.getString(R.string.total_time, minutesToDeduct.toString())
                .takeIf { minutesToDeduct > 0 }
                ?: context.getString(R.string.dont_calculate)
            Setting.MONTH_DATE_CALCULATIONS -> when (startDayCalculation) {
                1 -> context.getString(R.string.payment_cycle, startDayCalculation, 30)
                else -> context.getString(
                    R.string.payment_cycle,
                    startDayCalculation,
                    startDayCalculation - 1
                )
            }
            Setting.RATE_PER_DAY -> ""
            Setting.NOTIFY_ARRIVAL -> ""
            Setting.SICK_DAYS -> ""
        }
    }
}

enum class Setting(val title: Int, val icon: Int) {
    SALARY(R.string.salary, R.drawable.ic_salary),
    NOTIFY_ARRIVAL(R.string.reminders, R.drawable.ic_reminders),
    ADDITIONAL_HOURS_CALCULATION(R.string.additional_hours, R.drawable.additional_hours),
    TRAVELING_EXPENSES(R.string.travel_expense, R.drawable.ic_travelling_expenses),
    BREAKS(R.string.breaks, R.drawable.ic_breaks),
    MONTH_DATE_CALCULATIONS(R.string.calculation_period, R.drawable.ic_cycle_calculation),
    RATE_PER_DAY(R.string.saturday_rate, R.drawable.ic_saturday_rates),
    SICK_DAYS(R.string.sick_days, R.drawable.ic_sick_days)
}