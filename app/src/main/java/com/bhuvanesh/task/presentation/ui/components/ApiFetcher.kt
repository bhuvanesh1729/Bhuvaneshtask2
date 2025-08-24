package com.bhuvanesh.task.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bhuvanesh.network.ApiResult
import com.bhuvanesh.task.data.uidata.UIData
import com.bhuvanesh.task.data.uidata.UIState
import com.bhuvanesh.task.presentation.viewmodel.HoldingsViewModel
import org.jetbrains.annotations.ApiStatus

@Composable
fun ApiFetcher(
    modifier: Modifier,
    viewModel: HoldingsViewModel,
    composable: (@Composable (modifier: Modifier, data: UIData) -> Unit),
) {
    val state = viewModel.uiData.collectAsStateWithLifecycle()

    when (state.value) {
        UIState.Loading -> Box(modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }

        is UIState.Success<UIData> -> {
            composable(modifier, (state.value as UIState.Success<UIData>).data)
        }


        is UIState.Failure -> Box(modifier.fillMaxSize()) {
            Text(
                (state.value as UIState.Failure).error ?: "Something went wrong",
                modifier = Modifier
            )
        }
    }
}