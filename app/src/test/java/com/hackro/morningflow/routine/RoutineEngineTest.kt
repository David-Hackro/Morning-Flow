package com.hackro.morningflow.routine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutineEngineTest {
    private val steps = listOf(
        RoutineStep(
            title = "First step",
            instruction = "Do the first thing.",
            expectedDurationMinutes = 1,
        ),
        RoutineStep(
            title = "Second step",
            instruction = "Do the second thing.",
            expectedDurationMinutes = 2,
        ),
        RoutineStep(
            title = "Third step",
            instruction = "Do the third thing.",
            expectedDurationMinutes = 3,
        ),
    )

    @Test
    fun startBeginsAtFirstStep() {
        val engine = RoutineEngine(steps)

        val state = engine.start()

        assertEquals(1, state.currentPosition)
        assertEquals(3, state.totalSteps)
        assertEquals(steps.first(), state.currentStep)
    }

    @Test
    fun completeCurrentStepAdvancesExactlyOneStep() {
        val engine = RoutineEngine(steps)
        val firstState = engine.start()

        val secondState = engine.completeCurrentStep(firstState)

        assertTrue(secondState is RoutineState.Active)
        secondState as RoutineState.Active
        assertEquals(2, secondState.currentPosition)
        assertEquals(steps[1], secondState.currentStep)
    }

    @Test
    fun completeCurrentStepCompletesAfterFinalStep() {
        val engine = RoutineEngine(steps)
        val firstState = engine.start()
        val secondState = engine.completeCurrentStep(firstState)
        val thirdState = engine.completeCurrentStep(secondState)

        val completedState = engine.completeCurrentStep(thirdState)

        assertTrue(completedState is RoutineState.Completed)
        completedState as RoutineState.Completed
        assertEquals(3, completedState.totalSteps)
    }
}
