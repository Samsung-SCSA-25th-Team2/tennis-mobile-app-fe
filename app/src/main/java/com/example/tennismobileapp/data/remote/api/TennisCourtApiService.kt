package com.example.tennismobileapp.data.remote.api

import com.example.tennismobileapp.data.remote.dto.TennisCourtDto
import com.example.tennismobileapp.data.remote.dto.TennisCourtSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TennisCourtApiService {
    @GET("api/v1/courts/{id}")
    suspend fun getCourt(
        @Path("id") courtId: Long
    ) : TennisCourtDto

    @GET("api/v1/tennis-courts/search")
    suspend fun searchCourts(
        @Query("keyword") keyword: String = "서울",
        @Query("size") size: Int = 100,
        @Query("cursor") cursor: Int = 0
    ) : TennisCourtSearchResponseDto

}