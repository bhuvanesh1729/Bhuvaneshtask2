package com.bhuvanesh.task.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_holdings_table")
data class HoldingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

)