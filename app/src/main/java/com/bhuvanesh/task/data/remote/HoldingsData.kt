package com.bhuvanesh.task.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class HoldingsData(
    @Json(name = "data")
    val data: Content?
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "userHolding")
    val userHolding: List<Holdings?>?
)

@JsonClass(generateAdapter = true)
data class Holdings(
    @Json(name = "symbol")
    val symbol: String?,
    @Json(name = "quantity")
    val quantity: Int?,
    @Json(name = "ltp")
    val ltp: Double?,
    @Json(name = "avgPrice")
    val avgPrice: Double?,
    @Json(name = "close")
    val close: Double?
)


