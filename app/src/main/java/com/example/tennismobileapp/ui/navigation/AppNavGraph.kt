package com.example.tennismobileapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tennismobileapp.ui.court.list.CourtListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.CourtList.route,
        modifier = modifier
    ) {
        // 테니스장 목록 화면
        composable(NavigationRoute.CourtList.route) {
            CourtListScreen(
                onCourtClick = { courtId ->
                    navController.navigate(NavigationRoute.CourtDetail.createRoute(courtId)) }
            )
        }

        // 테니스장 상세 화면
//        composable(NavigationRoute.CourtDetail.route) { backStackEntry ->
//            val courtId = backStackEntry.arguments?.getLong("courtId") ?: 1L
//            CourtDetailRoute(
//                courtId = courtId,
//                onBackClick = { navController.popBackStack() }
//            )
//        }
    }
}