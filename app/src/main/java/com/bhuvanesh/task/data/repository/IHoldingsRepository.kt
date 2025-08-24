package com.bhuvanesh.task.data.repository

import com.bhuvanesh.network.ApiResult
import com.bhuvanesh.task.data.remote.HoldingsData
import kotlinx.coroutines.flow.Flow

interface IHoldingsRepository {
    suspend fun getHoldings(): Flow<ApiResult<HoldingsData>>
}