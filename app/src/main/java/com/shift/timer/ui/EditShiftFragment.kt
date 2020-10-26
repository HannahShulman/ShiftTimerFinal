package com.shift.timer.ui

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shift.timer.R
import com.shift.timer.dateToTimeFormat
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.extensions.extraNotNull
import com.shift.timer.inputToCents
import com.shift.timer.model.WageRatePercentage
import com.shift.timer.throttledClickListener
import com.shift.timer.viewmodels.EditShiftData
import com.shift.timer.viewmodels.EditShiftViewModel
import com.shift.timer.viewmodels.EditShiftViewModelFactory
import kotlinx.android.synthetic.main.dialog_edit_shift.*
import kotlinx.android.synthetic.main.entry_shit_edit_item_layout.*
import kotlinx.android.synthetic.main.exit_shift_edit_item_layout.*
import kotlinx.android.synthetic.main.rate_shift_edit_item_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditShiftFragment : BottomSheetDialogFragment() {

    companion object{
        const val SHIFT_ID_KEY = "id"
    }

    private val expansionLayoutCollection = ExpansionLayoutCollection()

    @Inject
    lateinit var factory: EditShiftViewModelFactory

    private val viewModel: EditShiftViewModel by viewModels { factory }

    private val shiftId: Int by extraNotNull(SHIFT_ID_KEY, -1)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { layout ->
                    val behaviour = BottomSheetBehavior.from(layout)
                    setupFullHeight(layout)
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }


    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_edit_shift, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expansionLayoutCollection.add(entry_expansion_layout)
        expansionLayoutCollection.add(exit_expansion_layout)
        expansionLayoutCollection.add(rate_expansion_layout)
        expansionLayoutCollection.add(bonus_expansion_layout)
        expansionLayoutCollection.openOnlyOne(true)

        with(view) {
            doOnLayout {
                // expand the bottom sheet dialog fully
                (dialog as? BottomSheetDialog)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    ?.let {
                        BottomSheetBehavior.from(it).apply {
                            state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getShiftById(shiftId).collect {
                //set data
                entry_value.text = it.start.dateToTimeFormat()
                entry_time_picker.setDefaultDate(it.start)
                exit_value.text = it.end?.dateToTimeFormat() ?: "---"
                exit_time_picker.setDefaultDate(it.end)
                rate_value.text = getString(
                    R.string.rate_percent, it.rate.value
                )
                shift_note.setText(it.note)
                total_payment.text = getString(
                    R.string.total_payment, it.getPaymentDisplay
                )

                rate_segments.check(
                    when (it.rate) {
                        WageRatePercentage.HUNDRED_PERCENT -> R.id.percent_100
                        WageRatePercentage.HUNDRED_TWENTY__FIVE_PERCENT -> R.id.percent_125
                        WageRatePercentage.HUNDRED_FIFTY_PERCENT -> R.id.percent_150
                        WageRatePercentage.TWO_HUNDRED -> R.id.percent_200
                    }
                )

            }
        }

        save_btn.throttledClickListener {
            viewModel.updateShiftData(buildEditData())
            targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, null)
            dismiss()
        }

        cancel_btn.throttledClickListener { dismiss() }
    }

    private fun buildEditData(): EditShiftData {
        val id = shiftId
        val rate = when (rate_segments.checkedRadioButtonId) {
            R.id.percent_100 -> 100
            R.id.percent_125 -> 125
            R.id.percent_150 -> 150
            R.id.percent_200 -> 200
            else -> 100
        }

        val bonus = bonus_et.text.toString().inputToCents()

        return EditShiftData(
            id,
            entry_time_picker.date,
            exit_time_picker.date,
            rate,
            bonus,
            shift_note.text.toString()
        )
    }
}