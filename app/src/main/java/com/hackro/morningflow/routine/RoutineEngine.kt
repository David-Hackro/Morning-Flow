package com.hackro.morningflow.routine

class RoutineEngine(
    private val routineSteps: List<RoutineStep> = defaultMorningRoutine(),
) {
    init {
        require(routineSteps.isNotEmpty()) { "Routine must contain at least one step." }
    }

    fun start(): RoutineState.Active = RoutineState.Active(
        steps = routineSteps,
        currentIndex = 0,
    )

    fun completeCurrentStep(state: RoutineState): RoutineState = when (state) {
        is RoutineState.Active -> {
            val nextIndex = state.currentIndex + 1
            if (nextIndex in state.steps.indices) {
                state.copy(currentIndex = nextIndex)
            } else {
                RoutineState.Completed(totalSteps = state.totalSteps)
            }
        }

        RoutineState.NotStarted,
        is RoutineState.Completed -> state
    }
}
