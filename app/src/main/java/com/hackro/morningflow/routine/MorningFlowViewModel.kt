package com.hackro.morningflow.routine

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MorningFlowViewModel(
    private val routineEngine: RoutineEngine = RoutineEngine(),
) : ViewModel() {
    private val _routineState = MutableStateFlow<RoutineState>(RoutineState.NotStarted)
    val routineState: StateFlow<RoutineState> = _routineState.asStateFlow()

    fun startRoutine() {
        _routineState.value = routineEngine.start()
    }

    fun completeCurrentStep() {
        _routineState.value = routineEngine.completeCurrentStep(_routineState.value)
    }
}
