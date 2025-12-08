package com.example.tennismobileapp.core.di

import android.media.AsyncPlayer
import com.example.tennismobileapp.core.network.ApiClient
import com.example.tennismobileapp.data.repository.TennisCourtRepositoryImpl
import com.example.tennismobileapp.domain.repository.TennisCourtRepository

object ServiceLocator {

    private val tennisCourtApi = ApiClient.tennisCourtApi

    val tennisCourtRepository : TennisCourtRepository by lazy {
        TennisCourtRepositoryImpl(tennisCourtApi) }
}