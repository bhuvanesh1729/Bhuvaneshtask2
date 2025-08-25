package com.bhuvanesh.task.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bhuvanesh.task.data.uidata.HoldingsUiData
import com.bhuvanesh.task.utils.toCurrency


@Composable
fun HoldingsListItem(holding: HoldingsUiData) {
    // Determine the color for the Profit & Loss text based on its value
    val pnlColor = if (holding.pnl >= 0) Color(0xFF1DBA72) else Color(0xFFBA1D1D)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp) // Added more horizontal padding
    ) {
        // --- Top Row: Symbol and Last Traded Price (LTP) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Stock Symbol (most important info)
            Text(
                text = holding.symbol,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            // LTP Label and Value
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "LTP: ",
                    style = MaterialTheme.typography.labelSmall, // Smaller size for the label
                    color = Color.Gray
                )
                Text(
                    text = "₹ ${holding.ltp.toCurrency()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // --- Bottom Row: Quantity and Profit & Loss (P&L) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Quantity Label and Value
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quantity: ",
                    style = MaterialTheme.typography.labelSmall, // Smaller size for the label
                    color = Color.Gray
                )
                Text(
                    text = "${holding.netQuantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            // P&L Label and Value
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "P&L: ",
                    style = MaterialTheme.typography.labelSmall, // Smaller size for the label
                    color = Color.Gray
                )
                Text(
                    text = "₹ ${holding.pnl.toCurrency()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = pnlColor, // Use the determined color
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    // Divider to separate items in a list
    Divider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
}