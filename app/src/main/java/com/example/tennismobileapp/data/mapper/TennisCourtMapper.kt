package com.example.tennismobileapp.data.mapper

import com.example.tennismobileapp.data.remote.dto.TennisCourtDto
import com.example.tennismobileapp.domain.model.TennisCourt

/**
 * DTO -> Domain Model
 *
 * - 앱 내부 비즈니스 로직에서 사용할 순수 도메인 모델로 변환 필요
 * - 확장함수 사용
 */
fun TennisCourtDto.toDomain() = TennisCourt(
    courtId = courtId,
    thumbnail = thumbnail,
    latitude = latitude,
    longitude = longitude,
    address = address,
    name = name
)