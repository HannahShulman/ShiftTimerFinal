package com.shift.timer.ui.workplace

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shift.timer.R
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.viewmodels.EditShiftViewModelFactory
import javax.inject.Inject

class NoAdditionalWorkplacesDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: EditShiftViewModelFactory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_no_additional_workplaces, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
}