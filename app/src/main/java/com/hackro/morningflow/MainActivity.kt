package com.hackro.morningflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackro.morningflow.routine.MorningFlowViewModel
import com.hackro.morningflow.routine.RoutineState
import com.hackro.morningflow.routine.RoutineStep
import com.hackro.morningflow.ui.theme.MorningFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MorningFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MorningFlowApp(
                        modifier = Modifier
                            .padding(innerPadding)
                            .safeDrawingPadding(),
                    )
                }
            }
        }
    }
}

@Composable
fun MorningFlowApp(
    modifier: Modifier = Modifier,
    viewModel: MorningFlowViewModel = viewModel(),
) {
    val routineState by viewModel.routineState.collectAsState()

    MorningFlowScreen(
        routineState = routineState,
        onStartRoutine = viewModel::startRoutine,
        onCompleteStep = viewModel::completeCurrentStep,
        modifier = modifier,
    )
}

@Composable
private fun MorningFlowScreen(
    routineState: RoutineState,
    onStartRoutine: () -> Unit,
    onCompleteStep: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (routineState) {
        RoutineState.NotStarted -> StartScreen(
            onStartRoutine = onStartRoutine,
            modifier = modifier,
        )

        is RoutineState.Active -> ActiveStepScreen(
            routineState = routineState,
            onCompleteStep = onCompleteStep,
            modifier = modifier,
        )

        is RoutineState.Completed -> CompletionScreen(
            totalSteps = routineState.totalSteps,
            modifier = modifier,
        )
    }
}

@Composable
private fun StartScreen(
    onStartRoutine: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Morning Flow",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Move through your morning routine one step at a time.",
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(
            onClick = onStartRoutine,
            modifier = Modifier.padding(top = 32.dp),
        ) {
            Text("Start routine")
        }
    }
}

@Composable
private fun ActiveStepScreen(
    routineState: RoutineState.Active,
    onCompleteStep: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val step = routineState.currentStep

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Step ${routineState.currentPosition} of ${routineState.totalSteps}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = step.title,
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = step.instruction,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Expected duration: ${step.expectedDurationMinutes} min",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(
            onClick = onCompleteStep,
            modifier = Modifier.padding(top = 32.dp),
        ) {
            Text("Complete")
        }
    }
}

@Composable
private fun CompletionScreen(
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Routine complete",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "You completed $totalSteps morning steps.",
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    MorningFlowTheme {
        StartScreen(onStartRoutine = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveStepScreenPreview() {
    MorningFlowTheme {
        ActiveStepScreen(
            routineState = RoutineState.Active(
                steps = listOf(
                    RoutineStep(
                        title = "Reach the bathroom",
                        instruction = "Get out of bed and walk directly to the bathroom.",
                        expectedDurationMinutes = 2,
                    ),
                ),
                currentIndex = 0,
            ),
            onCompleteStep = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompletionScreenPreview() {
    MorningFlowTheme {
        CompletionScreen(totalSteps = 10)
    }
}
