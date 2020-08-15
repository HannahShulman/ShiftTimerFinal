package com.shift.timer.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import at.grabner.circleprogress.AnimationState
import com.shift.timer.Constants.MAX_SECONDS_FOR_SHIFT
import com.shift.timer.Constants.hourMinuteDateFormat
import com.shift.timer.R
import com.shift.timer.SpContract
import com.shift.timer.animations.RevealAnimationSetting
import com.shift.timer.databinding.FragmentCurrentShiftBinding
import com.shift.timer.db.ShiftDao
import com.shift.timer.db.WorkplaceDao
import com.shift.timer.di.DaggerInjectHelper
import com.shift.timer.model.Shift
import com.shift.timer.model.Workplace
import com.shift.timer.throttledClickListener
import com.shift.timer.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CurrentShiftFragment : Fragment(R.layout.fragment_current_shift) {

    @Inject
    lateinit var factory: CurrentShiftViewModelFactory

    private val currentShiftViewModel: CurrentShiftViewModel by viewModels { factory }

    private var timeInShift = MutableStateFlow<Long?>(null)//count seconds

//    private var currentWorkplace = currentShiftViewModel.selectedWorkplace//MutableStateFlow(Workplace(description = "first working place"))

    private val binding: FragmentCurrentShiftBinding by viewBinding(FragmentCurrentShiftBinding::bind)

    private val timer = object : CountDownTimer(MAX_SECONDS_FOR_SHIFT.times(1000L), 1000) {
        override fun onFinish() {}

        override fun onTick(timeLeft: Long) {
            timeInShift.value = timeInShift.value?.plus(1) ?: 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerInjectHelper.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        settUI()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            setUserInteraction()
        }
    }

    //UI
    @SuppressLint("ClickableViewAccessibility")
    private fun settUI() {

        binding.infoIcon.throttledClickListener {
            MoreViewDialog.newInstance(constructRevealSettings()).show(parentFragmentManager, MoreViewDialog::class.java.name)
        }

        binding.progressCircular.isShowTextWhileSpinning = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.progressCircular.setBarColor(resources.getColor(R.color.cayanSelection, null),
                resources.getColor(R.color.mainCayan, null),
                resources.getColor(R.color.cayanSelection, null))
        } else {
            binding.progressCircular.setBarColor(resources.getColor(R.color.cayanSelection),
                resources.getColor(R.color.mainCayan),
                resources.getColor(R.color.cayanSelection))
        }

        var lastProgressValue = 0F
        binding.progressCircular.setOnAnimationStateChangedListener { it ->
            when (it) {
                AnimationState.IDLE -> {
                    val value = binding.progressCircular.currentValue
                    if (value == lastProgressValue) return@setOnAnimationStateChangedListener
                    lastProgressValue = value
                    viewLifecycleOwner.lifecycleScope.launch {
                        if (value == 100F) {
                            currentShiftViewModel.currentShift.take(1).collect {
                                if (it == null) {
                                    enterShift()
                                }
                            }
                        } else if (value == 0F) {
                            currentShiftViewModel.currentShift.take(1).collect {
                                it?.let { exitShift(it) }
                            }
                        }
                    }
                }
                AnimationState.SPINNING -> {
                }
                AnimationState.END_SPINNING -> {
                }
                AnimationState.END_SPINNING_START_ANIMATING -> {
                }
                AnimationState.START_ANIMATING_AFTER_SPINNING -> {
                }
                AnimationState.ANIMATING, null -> {
                }
            }

        }

        binding.progressCircular.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val newValue = if (binding.progressCircular.currentValue == 100F) 0F else 100F
                    binding.progressCircular.setValueAnimated(newValue, 1000)
                }
                MotionEvent.ACTION_UP -> {
                    if (binding.progressCircular.currentValue == 100F || binding.progressCircular.currentValue == 0F) return@setOnTouchListener true
                    viewLifecycleOwner.lifecycleScope.launch {
                        currentShiftViewModel.currentShift.take(1).collect {
                            val newValue = it?.let { 100F } ?: 0F
                            binding.progressCircular.setValueAnimated(newValue, 1000)
                        }
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    //observers
    private fun setObservers() {
        observeWorkplace()
        observeCurrentShift()
        observeTimeInShift()
    }

    private fun constructRevealSettings(): RevealAnimationSetting? {
        return RevealAnimationSetting.with(
            (binding.infoIcon.x + binding.infoIcon.width / 2).toInt(),
            (binding.infoIcon.y + binding.infoIcon.height / 2).toInt(),
            view?.width ?: 0,
            view?.height ?: 0)
    }

    private fun observeWorkplace() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            currentShiftViewModel.hasMultipleWorkplaces.collect {
                binding.workPlaceDescription.isVisible = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            currentShiftViewModel.selectedWorkplace.collect {
                binding.workPlaceDescription.text = it.description
            }
        }
    }

    private fun observeCurrentShift() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            currentShiftViewModel.currentShift.take(1).collect { shift ->
                if (shift != null) {
                    binding.progressCircular.setValue(100F)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            currentShiftViewModel.currentShift.collect { shift ->
                Log.d("TAG", "observeCurrentShift: $shift")
                binding.animation.isVisible = shift != null
                binding.exitShift.isVisible = shift != null
                binding.enterShift.isVisible = shift == null
                shift?.let {
                    binding.enterShift.clearAnimation()
                    binding.progressCircular.setText(it.start.toString())
                    binding.shiftStart.text = getString(R.string.entry_time, hourMinuteDateFormat.format(it.start))
                    timer.cancel()//ensure to block multiple processes of timer
                    timeInShift.value = Date().time.minus(it.start.time).div(1000)//convert to seconds
                    timer.start()
                } ?: run {
                    val anim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                    binding.enterShift.startAnimation(anim)
                    binding.progressCircular.setText(getString(R.string.not_in_shift))
                    binding.shiftStart.text = getString(R.string.not_in_shift)
                    timeInShift.value = null
                    timer.cancel()
                }
            }
        }
    }

    private fun observeTimeInShift() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            timeInShift.collect { inShift ->
                inShift?.let { time ->
                    binding.timeInShift.text = time.formattedTimeInShift()
                    if (time == currentShiftViewModel.shiftLengthInSeconds.value.toLong()) {
                        //show time to go home
                        Toast.makeText(context, "Time For Home", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                }
            }
        }
    }

    //Interaction
    private fun setUserInteraction() {
    }

    //functionality
    private suspend fun enterShift() {
        currentShiftViewModel.enterShift()
    }

    private suspend fun exitShift(shift: Shift) {
        currentShiftViewModel.endShift(shift)
    }

    private fun Long.formattedTimeInShift(): String {
        val seconds = this % 60
        val minutes = (this / 60) % 60
        val hours = (this / 60) / 60
        return getString(R.string.time_format, String.format("%02d", hours),
            String.format("%02d", minutes), String.format("%02d", seconds))
    }
}


class CurrentShiftViewModel(val repository: ShiftRepository, val workplaceRepository: WorkplaceRepository) : ViewModel() {

    suspend fun enterShift() {
        repository.startShift()
    }

    suspend fun endShift(shift: Shift) {
        repository.endShift(shift)
    }

    val currentShift = repository.getCurrentShift

    private val workplaces = workplaceRepository.workplaces

    val hasMultipleWorkplaces: Flow<Boolean> = workplaces.mapLatest { it.size > 1 }

    val shiftLengthInSeconds = MutableStateFlow(9.times(60).times(60))

    val selectedWorkplace: Flow<Workplace> = workplaceRepository.selectedWorkplace()//workplaceId.flatMapLatest { workplaceRepository.selectedWorkplace(it)?: MutableStateFlow(Workplace(description = "no workplace found") )}

}

class CurrentShiftViewModelFactory @Inject constructor(private val repository: ShiftRepository,
                                                       private val workplaceRepository: WorkplaceRepository)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentShiftViewModel(repository, workplaceRepository) as T
    }
}

class ShiftRepository @Inject constructor(private val spContract: SpContract,
                                          private val shiftDao: ShiftDao) {

    suspend fun startShift() {
        shiftDao.insertShift(Shift(workplaceId = spContract.workplaceId, start = Date(), end = null))
    }


    suspend fun endShift(shift: Shift) {
        shift.copy(end = Date()).also {
            shiftDao.endShift(it)
        }

    }

    val getCurrentShift = shiftDao.getCurrentShift()
}

class WorkplaceRepository @Inject constructor(private val spContract: SpContract, private val workplaceDao: WorkplaceDao) {

    val workplaces = workplaceDao.getAllWorkplaces()

    fun selectedWorkplace(): Flow<Workplace> {
        return workplaceDao.getWorkplaceById(spContract.workplaceId)
    }

}