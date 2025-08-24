package com.bhuvanesh.task.domain.usecases

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserPortfolioModule {
    @Binds
    abstract fun bindUserPortfolio(userPortfolio: UserPortfolio): IUserPortfolio
}