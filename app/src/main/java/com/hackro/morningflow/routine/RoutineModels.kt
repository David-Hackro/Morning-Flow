package com.hackro.morningflow.routine

data class RoutineStep(
    val title: String,
    val instruction: String,
    val expectedDurationMinutes: Int,
)

sealed interface RoutineState {
    data object NotStarted : RoutineState

    data class Active(
        val steps: List<RoutineStep>,
        val currentIndex: Int,
    ) : RoutineState {
        init {
            require(steps.isNotEmpty()) { "A running routine needs at least one step." }
            require(currentIndex in steps.indices) { "Current step index must point to an existing step." }
        }

        val currentStep: RoutineStep
            get() = steps[currentIndex]

        val currentPosition: Int
            get() = currentIndex + 1

        val totalSteps: Int
            get() = steps.size
    }

    data class Completed(
        val totalSteps: Int,
    ) : RoutineState
}

fun defaultMorningRoutine(): List<RoutineStep> = listOf(
    RoutineStep(
        title = "Reach the bathroom",
        instruction = "Get out of bed and walk directly to the bathroom.",
        expectedDurationMinutes = 2,
    ),
    RoutineStep(
        title = "Take a cold shower",
        instruction = "Take a quick cold shower to wake up fully.",
        expectedDurationMinutes = 5,
    ),
    RoutineStep(
        title = "Get dressed",
        instruction = "Put on the clothes you planned for the day.",
        expectedDurationMinutes = 5,
    ),
    RoutineStep(
        title = "Make the bed",
        instruction = "Reset the bed before leaving the room.",
        expectedDurationMinutes = 3,
    ),
    RoutineStep(
        title = "Meditate",
        instruction = "Sit down and meditate for ten minutes.",
        expectedDurationMinutes = 10,
    ),
    RoutineStep(
        title = "Drink water",
        instruction = "Drink a full glass of water.",
        expectedDurationMinutes = 2,
    ),
    RoutineStep(
        title = "Prepare breakfast",
        instruction = "Make a simple breakfast without checking other apps.",
        expectedDurationMinutes = 10,
    ),
    RoutineStep(
        title = "Eat breakfast",
        instruction = "Eat breakfast at a steady pace.",
        expectedDurationMinutes = 15,
    ),
    RoutineStep(
        title = "Walk in the park",
        instruction = "Go outside and walk in the park.",
        expectedDurationMinutes = 20,
    ),
    RoutineStep(
        title = "Read",
        instruction = "Read with your phone away.",
        expectedDurationMinutes = 15,
    ),
)
