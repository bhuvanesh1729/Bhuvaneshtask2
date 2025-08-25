package com.bhuvanesh.task.data.repository

import android.util.Log
import com.bhuvanesh.network.ApiResult
import com.bhuvanesh.network.awaitResult
import com.bhuvanesh.task.data.local.HoldingsDao
import com.bhuvanesh.task.data.local.toDbEntity
import com.bhuvanesh.task.data.local.toRemoteData
import com.bhuvanesh.task.data.remote.HoldingsData
import com.bhuvanesh.task.data.remote.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HoldingsRepository @Inject constructor(
    private val portfolioService: UserService,
    private val holdingDao: HoldingsDao,
) : IHoldingsRepository {

    override suspend fun getHoldings(): Flow<ApiResult<HoldingsData>> {
        return flow {
            // 1. Emit cached data first.
            val localData = holdingDao.getHoldings()
            if (localData.isNotEmpty()) {
                Log.d("TAG", "Emitting data from local DB")
                emit(ApiResult.Success(localData.toRemoteData()))
            }

            // 2. Fetch fresh data from the network.
            Log.d("TAG", "Fetching data from remote network")
            when (val remoteData = portfolioService.getUserData().awaitResult()) {
                is ApiResult.Success -> {
                    // 3. On success, update the database.
                    // The UI will get this new data from its DB observer.
                    remoteData.data.toDbEntity()?.let {
                        Log.d("TAG", "Saving remote data to DB")
                        holdingDao.insert(it)
                    }
                    emit(remoteData)
                }

                else -> {
                    // 4. On failure, only emit an error if there's no cached data to show.
                    if (localData.isEmpty()) {
                        Log.d("TAG", "Emitting error as DB is empty")
                        emit(remoteData)
                    }

                }

            }
        }
    }
}