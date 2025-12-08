package com.example.tennismobileapp.ui.navigation

sealed class NavigationRoute(val route: String) {
    // sealed class :  상속가능한 Enum 타입

    // object : 싱글톤 하위클래스
    object CourtList : NavigationRoute("courtList")

    object CourtDetail : NavigationRoute("courtDetail/{courtId}") {
        fun createRoute(courtId: Long): String = "courtDetail/$courtId"
    }
}
