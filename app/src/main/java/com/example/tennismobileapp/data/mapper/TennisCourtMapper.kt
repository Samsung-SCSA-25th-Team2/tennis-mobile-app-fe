package com.example.tennismobileapp.data.mapper

import com.example.tennismobileapp.data.remote.dto.TennisCourtDto
import com.example.tennismobileapp.domain.model.TennisCourt

fun TennisCourtDto.toDomain() = TennisCourt(
    courtId = courtId,
    thumbnail = thumbnail,
    latitude = latitude,
    longitude = longitude,
    address = address,
    name = name
)