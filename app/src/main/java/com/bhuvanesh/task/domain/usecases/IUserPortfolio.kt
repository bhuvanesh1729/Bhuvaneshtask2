package com.bhuvanesh.task.domain.usecases

import com.bhuvanesh.task.data.uidata.UIData
import com.bhuvanesh.task.data.uidata.UIState
import kotlinx.coroutines.flow.Flow

interface IUserPortfolio {
    suspend fun invoke(): Flow<UIState<UIData>>
}