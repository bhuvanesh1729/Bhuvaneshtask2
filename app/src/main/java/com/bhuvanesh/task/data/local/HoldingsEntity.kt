package com.bhuvanesh.task.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bhuvanesh.task.data.remote.Content
import com.bhuvanesh.task.data.remote.Holdings
import com.bhuvanesh.task.data.remote.HoldingsData

@Entity(tableName = "user_holdings_table")
data class HoldingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)


fun HoldingsData.toDbEntity(): List<HoldingsEntity>? {

    return this.data?.userHolding
        ?.filterNotNull()
        ?.map {
            HoldingsEntity(
                symbol = it.symbol ?: "",
                quantity = it.quantity ?: 0,
                ltp = it.ltp ?: 0.0,
                close = it.close ?: 0.0,
                avgPrice = it.avgPrice ?: 0.0,
            )
        } ?: emptyList()
}


fun List<HoldingsEntity>.toRemoteData(): HoldingsData {
    return HoldingsData(
        data = Content(
            userHolding = this.map { entity ->
                Holdings(
                    symbol = entity.symbol,
                    quantity = entity.quantity,
                    ltp = entity.ltp,
                    avgPrice = entity.avgPrice,
                    close = entity.close,
                )
            }
        )
    )
}