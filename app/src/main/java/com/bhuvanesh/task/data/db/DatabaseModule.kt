package com.bhuvanesh.task.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "user_holdings" // Database file name

    /**
     * Provides a singleton instance of the AppDatabase.
     * Hilt will manage the lifecycle of this instance.
     * @param context The application context provided by Hilt.
     * @return An instance of AppDatabase.
     */
    @Provides
    @Singleton // Ensures only one instance of the database is created
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }


}