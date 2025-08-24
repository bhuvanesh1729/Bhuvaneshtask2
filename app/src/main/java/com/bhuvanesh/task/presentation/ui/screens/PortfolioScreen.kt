package com.bhuvanesh.task.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bhuvanesh.task.data.uidata.HoldingsUiData
import com.bhuvanesh.task.data.uidata.UIData
import com.bhuvanesh.task.presentation.ui.components.HoldingsListItem
import com.bhuvanesh.task.presentation.ui.components.TotalInvestmentView

@Composable
fun PortfolioScreen(modifier: Modifier, data: UIData) {
    return Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        Column {
            Holdings(modifier = Modifier.weight(1f), data.holdings)
            TotalInvestmentView(data.totalInvestmentUiData)
        }
    }
}


@Composable
fun Holdings(
    modifier: Modifier,
    holdings: List<HoldingsUiData>,
) {
    LaunchedEffect(holdings) {
        println("change in holding detected")
    }
    LazyColumn(modifier = modifier) {
        items(
            items = holdings,
            key = { holding -> holding.symbol },
            contentType = { it.symbol }) { h ->
            HoldingsListItem(h)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.LightGray)
            )
        }
    }
}
