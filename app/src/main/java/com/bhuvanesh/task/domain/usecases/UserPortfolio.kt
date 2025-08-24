package com.bhuvanesh.task.domain.usecases

import android.util.Log
import com.bhuvanesh.network.ApiResult
import com.bhuvanesh.task.data.repository.HoldingsRepository
import com.bhuvanesh.task.data.uidata.HoldingsUiData
import com.bhuvanesh.task.data.uidata.TotalInvestmentUiData
import com.bhuvanesh.task.data.uidata.UIData
import com.bhuvanesh.task.data.uidata.UIState
import com.bhuvanesh.task.data.uidata.calculateTotals
import com.bhuvanesh.task.data.uidata.toHoldingsUiDataList
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class UserPortfolio @Inject constructor(private val holdingsRepository: HoldingsRepository) :
    IUserPortfolio {
    override suspend fun invoke(): Flow<UIState<UIData>> {
        return holdingsRepository.getHoldings()
            .map { apiResult ->
                // Transform ApiResult<T> into UIState<T>
                when (apiResult) {
                    is ApiResult.Success -> {
                        val userHoldings = apiResult.data.data?.userHolding
                            ?.toHoldingsUiDataList() ?: emptyList()

                        val totalInvestments = userHoldings.calculateTotals()

                        UIState.Success(UIData(userHoldings, totalInvestments))
                    }
                    is ApiResult.Error -> UIState.Failure(apiResult.message)
                    is ApiResult.Exception -> UIState.Failure(apiResult.e.localizedMessage ?: "An unexpected error occurred")
                    ApiResult.Loading -> UIState.Loading // Assuming you want to propagate this
                }
            }
            .onStart { emit(UIState.Loading) } // A better way to signal start of the operation
            .catch { e ->
                // Catch any unexpected exceptions from the flow itself
                Log.e("UserPortfolio", "Flow collection error", e)
                emit(UIState.Failure(e.localizedMessage ?: "An unexpected error occurred"))
            }
    }
}