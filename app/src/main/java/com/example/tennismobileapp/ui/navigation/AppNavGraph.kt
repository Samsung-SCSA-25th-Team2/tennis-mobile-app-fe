package com.example.tennismobileapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tennismobileapp.ui.court.detail.CourtDetailRoute
import com.example.tennismobileapp.ui.court.list.CourtListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    // navController 받음
    modifier: Modifier = Modifier
    // modifier 받음
) {
    /**
     * `NavHost`
     * - 네비게이션의 Home 화면(시작 지점) 지정
     * - 각 Route에 어떤 화면이 연결되는지 등록
     */
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.CourtList.route, // 첫 앱화면
        modifier = modifier
    ) {
        // 테니스장 목록 화면
        composable(
            route = NavigationRoute.CourtList.route
            // 싱글톤 객체로 URI를 정해둠 -> 오류 최소화 가능케 함
        ) {
            CourtListScreen(
                // CourtListScreen 화면 띄우기

                onCourtClick = { courtId ->
                    navController.navigate(NavigationRoute.CourtDetail.createRoute(courtId))
                }
                // 리스트에서 아이템을 클릭하면 (CourtId)를 입력값으로 상세화면으로 URI 만들어서 이동
            )
        }

        // 테니스장 상세 화면
        composable(
            route = NavigationRoute.CourtDetail.route,
            arguments = listOf(
                // 인자가 하나여도, listOf를 사용
                navArgument("courtId") {
                    // 인자의 이름을 정의

                    type = NavType.LongType
                    // Long 타입으로 강제
                }
            )
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getLong("courtId") ?: 1L
            // navController가 인자로 던진 값, Long으로 자동으로 parsing된 값 읽기

            CourtDetailRoute(
                courtId = courtId,
                // 코트Id가 필요함

                onBackClick = { navController.popBackStack() }
                // CourtDetailRoute 기준으로, 뒤로 가기 누르면 목록 화면으로 돌아감
            )
        }
    }
}