package com.example.tennismobileapp.data.repository

import com.example.tennismobileapp.data.mapper.toDomain
import com.example.tennismobileapp.data.remote.api.TennisCourtApiService
import com.example.tennismobileapp.domain.model.TennisCourt
import com.example.tennismobileapp.domain.repository.TennisCourtRepository

class TennisCourtRepositoryImpl(
    private val tennisCourtApiService: TennisCourtApiService
) : TennisCourtRepository
{
    override suspend fun getCourt(courtId: Long): TennisCourt {
        return tennisCourtApiService.getCourt(courtId).toDomain()
    }

    override suspend fun getAllCourts(): List<TennisCourt> {
        return tennisCourtApiService.searchCourts().courts.map { it.toDomain() }
    }
}