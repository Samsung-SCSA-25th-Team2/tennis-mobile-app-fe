package com.example.tennismobileapp.core.di

import com.example.tennismobileapp.core.network.ApiClient
import com.example.tennismobileapp.data.repository.TennisCourtRepositoryImpl
import com.example.tennismobileapp.domain.repository.TennisCourtRepository

/**
 * ServiceLocator : DI Container
 *
 * - Repository와 API 객체를 관리
 * - object : 싱글톤
 */
object ServiceLocator {

    // private으로 하여, repository 만 api에 접근하도록 함
    private val tennisCourtApi = ApiClient.tennisCourtApi

    // 구현체를 사용하고, by lazy 로 재사용하게 함.
    val tennisCourtRepository : TennisCourtRepository by lazy {
        TennisCourtRepositoryImpl(tennisCourtApi) }
}