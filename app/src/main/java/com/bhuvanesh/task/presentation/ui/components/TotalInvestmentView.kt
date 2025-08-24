package com.bhuvanesh.task.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhuvanesh.task.data.uidata.TotalInvestmentUiData
import com.bhuvanesh.task.utils.toCurrency

@Composable
fun TotalInvestmentView(
    aggregatedData: TotalInvestmentUiData,
) {
    val pnl = aggregatedData.totalPnL
    val pnlColor = if (pnl >= 0) Color(0xFF1DBA72) else Color(0xFFBA1D1D)
    val todayPnlColor = if (aggregatedData.todayPnL >= 0) Color(0xFF1DBA72) else Color(0xFFBA1D1D)
    val percentage = if (aggregatedData.totalInvestment > 0)
        (pnl * 100) / aggregatedData.totalInvestment
    else 0.0
    val percentageText = String.format("(%.2f%%)", percentage)
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavigationBarDefaults.containerColor)
            .padding(16.dp)
            .clickable{
                isExpanded = !isExpanded
            }
    ) {
        if (isExpanded) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Current value*",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "₹ ${aggregatedData.currentValue.toCurrency()}",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Total investment*",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "₹ ${aggregatedData.totalInvestment.toCurrency()}",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Today's Profit & Loss*",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    (if (aggregatedData.todayPnL >= 0) "₹${aggregatedData.todayPnL.toCurrency()}" else "-₹${(-aggregatedData.todayPnL).toCurrency()}"),
                    color = todayPnlColor,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Profit & Loss*",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    (if (pnl >= 0) "₹${pnl.toCurrency()}" else "-₹${(-pnl).toCurrency()}"),
                    color = pnlColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    " $percentageText",
                    color = pnlColor,
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}