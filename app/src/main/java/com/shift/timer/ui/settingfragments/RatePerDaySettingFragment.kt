package com.shift.timer.ui.settingfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection
import com.shift.timer.R
import com.shift.timer.dao.DayOfWeekWithRate
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.getDayOfWeekRepresentation
import info.hoang8f.android.segmented.SegmentedGroup
import kotlinx.android.synthetic.main.rate_per_day_setting_fragment.*

class RatePerDaySettingFragment : SettingBaseFragment(R.layout.rate_per_day_setting_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rate_list.adapter = DayRateAdapter(::onRatePerDaySelected)
    }

    fun onRatePerDaySelected(dayOfWeek: Int, percent: Int) {
        (rate_list.adapter as DayRateAdapter).updateDayRate(dayOfWeek, percent)
        Toast.makeText(
            requireContext(),
            "percent $percent  position: ${dayOfWeek.getDayOfWeekRepresentation()}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun saveSetting() {
       val list =  (rate_list.adapter as DayRateAdapter).changedSetting.map {
            DayOfWeekWithRate(
                it.key,
                it.value
            )
        }
        settingsViewModel.updateRatePerDateSetting(list)
    }
}

class DayRateAdapter(val percentageClicked: (dayOfWeek: Int, percent: Int) -> Unit) :
    RecyclerView.Adapter<DayRateAdapter.ViewHolder>() {

    val changedSetting = HashMap<Int, Int>()

    fun updateDayRate(dayOfWeek: Int, percent: Int) {
//        changedSetting[dayOfWeek] = percent
    }

    var sundayRate: Int = 100
        set(value) {
            field = value
            notifyItemChanged(0)
        }
    var mondayRate: Int = 100
        set(value) {
            field = value
            notifyItemChanged(1)
        }
    var tuesdayRate: Int = 100
        set(value) {
            field = value
            notifyItemChanged(2)
        }
    var wednesdayRate: Int = 100
        set(value) {
            field = value
            notifyItemChanged(3)
        }
    var thursdayRate: Int = 100
        set(value) {
            field = value
            notifyItemChanged(4)
        }
    var fridayRate: Int = 150
        set(value) {
            field = value
            notifyItemChanged(5)
        }
    var saturdayRate: Int = 150
        set(value) {
            field = value
            notifyItemChanged(6)
        }

    private val collection = ExpansionLayoutCollection().apply {
        openOnlyOne(false)
    }

    inner class ViewHolder(
        itemView: View,
        val percentageClicked: (position: Int, percent: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {
        val expansionLayout: ExpansionLayout =
            itemView.findViewById(R.id.rate_expansion_layout)
        val rateSegmentedGroup: SegmentedGroup =
            itemView.findViewById<SegmentedGroup>(R.id.rate_segments)
        val rateValue: TextView = itemView.findViewById(R.id.rate_value)
        val dayOfWeek: TextView = itemView.findViewById(R.id.day_of_week)

        init {
            rateSegmentedGroup.setOnCheckedChangeListener { _, id ->
                val value = when (id) {
                    R.id.percent_100 -> 100
                    R.id.percent_125 -> 125
                    R.id.percent_150 -> 150
                    R.id.percent_200 -> 200
                    else -> 100
                }
                changedSetting[absoluteAdapterPosition + 1] = value
                rateValue.text = itemView.context.getString(R.string.rate_percent, value)
                percentageClicked(absoluteAdapterPosition + 1, value)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.single_rate_item, parent, false),
            percentageClicked
        )
    }

    override fun getItemCount(): Int = 7

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        collection.add(holder.expansionLayout)
        holder.dayOfWeek.text = (position + 1).getDayOfWeekRepresentation()
        holder.rateValue.text = context.getString(
            R.string.rate_percent, when (position) {
                0 -> sundayRate
                1 -> mondayRate
                2 -> tuesdayRate
                3 -> wednesdayRate
                4 -> thursdayRate
                5 -> fridayRate
                6 -> saturdayRate
                else -> 0
            }
        )
        holder.rateSegmentedGroup.check(
            when (position) {
                0 -> when (sundayRate) {
                    100 -> R.id.percent_100
                    125 -> R.id.percent_125
                    150 -> R.id.percent_150
                    200 -> R.id.percent_200
                    else -> R.id.percent_100
                }
                1 -> when (mondayRate) {
                    100 -> R.id.percent_100
                    125 -> R.id.percent_125
                    150 -> R.id.percent_150
                    200 -> R.id.percent_200
                    else -> R.id.percent_100
                }
                2 -> when (tuesdayRate) {
                    100 -> R.id.percent_100
                    125 -> R.id.percent_125
                    150 -> R.id.percent_150
                    200 -> R.id.percent_200
                    else -> R.id.percent_100
                }
                3 -> when (wednesdayRate) {
                    100 -> R.id.percent_100
                    125 -> R.id.percent_125
                    150 -> R.id.percent_150
                    200 -> R.id.percent_200
                    else -> R.id.percent_100
                }
                4 -> when (thursdayRate) {
                    100 -> R.id.percent_100
                    125 -> R.id.percent_125
                    150 -> R.id.percent_150
                    200 -> R.id.percent_200
                    else -> R.id.percent_100
                }
                5 -> when (fridayRate) {
                    100 -> R.id.percent_100
                    125 -> R.id.percent_125
                    150 -> R.id.percent_150
                    200 -> R.id.percent_200
                    else -> R.id.percent_100
                }
                6 -> when (saturdayRate) {
                    100 -> R.id.percent_100
                    125 -> R.id.percent_125
                    150 -> R.id.percent_150
                    200 -> R.id.percent_200
                    else -> R.id.percent_100
                }
                else -> 0
            }
        )

    }
}