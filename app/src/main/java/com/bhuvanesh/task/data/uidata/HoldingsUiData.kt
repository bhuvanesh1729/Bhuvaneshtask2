package com.bhuvanesh.task.data.uidata

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.bhuvanesh.task.data.remote.Holdings
import com.bhuvanesh.task.data.remote.HoldingsData

data class HoldingsUiData(
    val symbol: String,
    val ltp: Double,
    val netQuantity: Int,
    val avgPrice: Double,
    val close: Double
) {
    val pnl: Double = (ltp - avgPrice) * netQuantity
    val totalInvestment: Double = avgPrice * netQuantity
    val todayPnl: Double = (ltp - close) * netQuantity
}

data class UIData(
    val holdings: List<HoldingsUiData>, val totalInvestmentUiData: TotalInvestmentUiData
)


data class TotalInvestmentUiData(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPnL: Double,
    val todayPnL: Double
)


sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    class Success<out T>(val data: T) : UIState<T>()
    class Failure(val error: String?) : UIState<Nothing>()
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    companion object {
        val tabs = listOf<BottomNavigationItem>(
            BottomNavigationItem(
                "Portfolio",
                Icons.Outlined.List,
                Icons.Outlined.List,
            )
        )
    }
}


// In a mapper file, e.g., HoldingsMapper.kt

/**
 * Maps a list of network DTOs to a list of UI data models.
 * It also filters out any invalid entries.
 */
fun List<Holdings?>.toHoldingsUiDataList(): List<HoldingsUiData> {
    return this.filterNotNull()
        .map { dto ->
            HoldingsUiData(
                symbol = dto.symbol ?: "",
                ltp = dto.ltp ?: 0.0,
                netQuantity = dto.quantity ?: 0,
                avgPrice = dto.avgPrice ?: 0.0,
                close = dto.close ?: 0.0
            )
        }
        .filter { it.symbol.isNotBlank() } // Ensure we only have valid symbols
}

/**
 * Calculates the total portfolio values from a list of holdings.
 */
fun List<HoldingsUiData>.calculateTotals(): TotalInvestmentUiData {
    return TotalInvestmentUiData(
        currentValue = sumOf { it.ltp * it.netQuantity },
        totalInvestment = this.sumOf { it.totalInvestment },
        totalPnL = this.sumOf { it.pnl },
        todayPnL = this.sumOf { it.todayPnl }
    )
}