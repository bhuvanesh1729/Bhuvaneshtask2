package com.bhuvanesh.task.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HoldingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(holding: HoldingsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(holdings: List<HoldingsEntity>)

    @Query("SELECT * FROM user_holdings_table")
    suspend fun getHoldings(): List<HoldingsEntity>

}