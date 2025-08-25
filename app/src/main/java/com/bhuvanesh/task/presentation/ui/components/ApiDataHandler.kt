import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bhuvanesh.task.data.uidata.UIState


/**
 * A generic composable to handle displaying UI based on the state of an API call.
 *
 * @param T The type of data to be displayed in the success state.
 * @param uiState The state flow representing the current UI state (Loading, Success, Failure).
 * @param modifier The modifier to be applied to the root container.
 * @param onRefresh A lambda function to be invoked when the user requests a data refresh.
 * @param onSuccessContent The composable content to display when the data is successfully loaded.
 */
@Composable
fun <T> ApiDataHandler(
    modifier: Modifier = Modifier,
    uiState: UIState<T>,
    onRefresh: () -> Unit,
    onSuccessContent: @Composable (modifier: Modifier,data: T) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            UIState.Loading -> {
                CircularProgressIndicator()
            }

            is UIState.Success<T> -> {
                // The state is smart-cast to Success, so we can directly access uiState.data
                onSuccessContent(modifier,uiState.data)
            }

            is UIState.Failure -> {
                // The state is smart-cast to Failure
                ErrorView(
                    errorMessage = uiState.error ?: "An unexpected error occurred.",
                    onRetry = onRefresh // Pass the refresh lambda to the ErrorView
                )
            }
        }
    }
}

@Composable
private fun ErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error Icon",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}