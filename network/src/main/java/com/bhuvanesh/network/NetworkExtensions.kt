package com.bhuvanesh.network

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

sealed class ApiResult<T : Any> {
    class Success<T: Any>(val data: T) : ApiResult<T>()
    class Error<T: Any>(val code: Int, val message: String?) : ApiResult<T>()
    class Exception<T: Any>(val e: Throwable) : ApiResult<T>()

    object Loading: ApiResult<Unit>()
}

/**
 * An improved extension function on Retrofit's Call<T> that properly
 * integrates with coroutine cancellation.
 *
 * It uses suspendCancellableCoroutine to bridge the callback-based 'enqueue'
 * method with the suspend/resume mechanism of coroutines.
 */
suspend fun <T : Any> Call<T>.awaitResult(): ApiResult<T> {
    // This is the core of the solution.
    return suspendCancellableCoroutine { continuation ->

        // This block is key. If the coroutine is cancelled,
        // it triggers Retrofit's own cancel method on the Call object.
        continuation.invokeOnCancellation {
            // 'this' refers to the Call<T> object.
            cancel()
        }

        // We use enqueue for its asynchronous nature, which fits perfectly
        // with the suspend/resume model of coroutines.
        enqueue(object : Callback<T> {

            // This is called when the HTTP response is received.
            override fun onResponse(call: Call<T>, response: Response<T>) {

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(ApiResult.Success(body))
                    } else {
                        val e = NullPointerException("Response body is null")
                        continuation.resume(ApiResult.Exception(e))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    continuation.resume(ApiResult.Error(code = response.code(), message = errorBody))
                }
            }

            // This is called when a network exception occurs or the request is cancelled.
            override fun onFailure(call: Call<T>, t: Throwable) {
                // Don't resume if the coroutine was already cancelled.
                if (continuation.isCancelled) return

                continuation.resume(ApiResult.Exception(t))
            }
        })
    }
}