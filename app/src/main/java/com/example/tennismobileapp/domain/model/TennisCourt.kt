package com.example.tennismobileapp.domain.model

data class TennisCourt(
    val courtId : Long,
    val thumbnail: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val name: String
)
