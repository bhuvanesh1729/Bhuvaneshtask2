package com.bhuvanesh.task.data

import com.bhuvanesh.task.data.local.HoldingsDao
import com.bhuvanesh.task.data.remote.UserService
import com.bhuvanesh.task.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class DataProvider {

    /**
    * This provider is scoped to the ViewModel.
    * A new instance of ProfileApiService will be created for each
    * Hilt ViewModel instance.
    */
    @Provides
    @ViewModelScoped
    fun provideProfileApiService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    /**
     * Provides an instance of HoldingDao.
     * This method depends on AppDatabase, which Hilt knows how to provide
     * @param appDatabase The database instance.
     * @return An instance of HoldingDao.
     */
    @Provides
    @ViewModelScoped
    fun provideHoldingDao(appDatabase: AppDatabase): HoldingsDao {
        return appDatabase.holdingDao()
    }
}