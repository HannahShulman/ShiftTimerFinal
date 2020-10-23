package com.shift.timer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.withSpringForceProperties
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shift.timer.R
import com.shift.timer.animations.AnimationFinishedListener
import com.shift.timer.animations.AnimationUtils
import com.shift.timer.animations.Dismissible
import com.shift.timer.animations.RevealAnimationSetting
import com.shift.timer.databinding.FragmentCompletedShiftBinding
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.model.Shift
import com.shift.timer.model.totalTimeInMinutes
import com.shift.timer.throttledClickListener
import com.shift.timer.viewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CompletedShiftFragment : DialogFragment(), Dismissible {

    private val binding: FragmentCompletedShiftBinding by viewBinding(FragmentCompletedShiftBinding::bind)

    @Inject
    lateinit var factory: CompletedShiftViewModelFactory

    private val completedShiftViewModel: CompletedShiftViewModel by viewModels { factory }

    override fun getTheme(): Int = R.style.full_sheet_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed_shift, container)
        AnimationUtils.registerCircularRevealAnimation(
            context,
            view,
            requireArguments().getSerializable(ARG_REVEAL_SETTINGS) as RevealAnimationSetting?,
            ContextCompat.getColor(requireContext(), R.color.cayanSelection),
            ContextCompat.getColor(requireContext(), R.color.cayanSelection)
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            completedShiftViewModel.getShiftById(requireArguments().getInt(SHIFT_ID)).collect {
                binding.shiftSummary.runTranslationAnimation(0f, 1.4f) {
                    binding.entryValue.text =
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.start)
                    it.end?.let { endDate ->
                        binding.exitValue.text =
                            SimpleDateFormat("HH:mm", Locale.getDefault()).format(endDate)
                    }
                    binding.rateValue.text =
                        getString(R.string.rate_percent, it.rate.value)//it.rate.value)
                    binding.totalTimeValue.text =
                        getString(R.string.total_time, it.totalTimeInMinutes().toString())
                    binding.totalPaymentValue.text = getString(
                        R.string.total_payment,
                        String.format(Locale.getDefault(), "%.2f", it.payment.div(100.0).toFloat())
                    )
                }
            }
        }

        binding.editShift.throttledClickListener {
            EditShiftFragment().show(
                parentFragmentManager,
                EditShiftFragment::class.java.name
            )
        }

        binding.okButton.throttledClickListener {
            dismiss(object : Dismissible.OnDismissedListener {
                override fun onDismissed() {
                    if (isAdded) {
                        parentFragmentManager.beginTransaction().remove(this@CompletedShiftFragment)
                            .commitAllowingStateLoss()
                    }
                }
            })
        }
    }

    private fun View.runTranslationAnimation(start: Float = 0F, des: Float, block: () -> Unit) {

        isVisible = true
        val anim = SpringAnimation(this, DynamicAnimation.TRANSLATION_Y)
            .setStartValue(start)
            .withSpringForceProperties {
                dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                stiffness = SpringForce.STIFFNESS_VERY_LOW
                finalPosition = resources.displayMetrics.heightPixels.toFloat().div(des)
            }
        anim.start()
        block()
    }

    companion object {

        const val SHIFT_ID = "shift_id"
        const val ARG_REVEAL_SETTINGS = "ARG_REVEAL_SETTINGS"

        fun newInstance(shiftId: Int, setting: RevealAnimationSetting?): CompletedShiftFragment {
            return CompletedShiftFragment().apply {
                setting?.let {
                    arguments = bundleOf(
                        SHIFT_ID to shiftId,
                        ARG_REVEAL_SETTINGS to it
                    )
                }
            }
        }
    }

    override fun dismiss(listener: Dismissible.OnDismissedListener?) {
        AnimationUtils.startCircularRevealExitAnimation(requireContext(),
            requireView(),
            requireArguments().getSerializable(ARG_REVEAL_SETTINGS) as RevealAnimationSetting,
            ContextCompat.getColor(requireContext(), R.color.cayanSelection),
            ContextCompat.getColor(requireContext(), R.color.cayanSelection),
            object : AnimationFinishedListener {
                override fun onAnimationFinished() {
                    listener?.onDismissed()
                }
            })
    }
}


class CompletedShiftViewModel(
    private val repository: ShiftRepository,
    val workplaceRepository: WorkplaceRepository
) : ViewModel() {

    fun getShiftById(id: Int): Flow<Shift> = repository.getShiftById(id)
}

class CompletedShiftViewModelFactory @Inject constructor(
    private val repository: ShiftRepository,
    private val workplaceRepository: WorkplaceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompletedShiftViewModel(repository, workplaceRepository) as T
    }
}