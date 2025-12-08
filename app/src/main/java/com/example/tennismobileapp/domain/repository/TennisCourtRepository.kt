package com.example.tennismobileapp.domain.repository

import com.example.tennismobileapp.domain.model.TennisCourt

interface TennisCourtRepository {
    suspend fun getCourt(courtId : Long) : TennisCourt
    suspend fun getAllCourts() : List<TennisCourt>
}