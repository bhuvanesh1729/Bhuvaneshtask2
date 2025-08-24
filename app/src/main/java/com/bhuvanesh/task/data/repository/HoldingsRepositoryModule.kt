package com.bhuvanesh.task.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class HoldingsRepositoryModule {
    @Binds
    abstract fun bindHoldingsRepository(holdingsRepository: HoldingsRepository): IHoldingsRepository
}