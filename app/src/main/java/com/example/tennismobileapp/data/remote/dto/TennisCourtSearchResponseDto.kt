package com.example.tennismobileapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TennisCourtSearchResponseDto(
    @Json(name = "courts")
    val courts: List<TennisCourtDto>,

    @Json(name = "hasNext")
    val hasNext: Boolean,

    @Json(name = "cursor")
    val cursor: Int,

    @Json(name = "size")
    val size: Int
)
