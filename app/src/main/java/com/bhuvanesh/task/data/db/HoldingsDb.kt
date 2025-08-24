package com.bhuvanesh.task.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bhuvanesh.task.data.local.HoldingsDao
import com.bhuvanesh.task.data.local.HoldingsEntity

@Database(entities = [HoldingsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holdingDao(): HoldingsDao
}