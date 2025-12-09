package com.example.tennismobileapp.ui.navigation

/**
 * Navigation route 문자열을 문자열 하드코딩으로 쓰지 않고
 * 타입 안전하게 관리하기 위한 패턴이다.
 */
sealed class NavigationRoute(val route: String) {
    // sealed class :  상속가능한 Enum 타입

    // object : 싱글톤 하위클래스
    object CourtList : NavigationRoute("courtList")

    object CourtDetail : NavigationRoute("courtDetail/{courtId}") {
        fun createRoute(courtId: Long): String = "courtDetail/${courtId}"
        // 동적 route를 만들기 위한 도우미함수
    }
}
