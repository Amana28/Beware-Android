package com.example.beware.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beware.ui.screen.beware.BewareScreen
import com.example.beware.ui.screen.login.LoginScreen
import com.example.beware.ui.screen.profile.ProfileScreen
import com.example.beware.ui.screen.report.ReportScreen
import com.example.beware.ui.screen.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // navigate to the Beware screen
                    navController.navigate(Screen.Beware.route)
                }
            )
        }
        composable(Screen.Beware.route) {
            BewareScreen(
                onProfileIconClick = {
                    // navigate to Profile Screen
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onCreateNewReportClick = {
                    navController.navigate(Screen.Report.route)
                }
            )
        }
        composable(Screen.Report.route) {
            ReportScreen(
                onReportIncidentSuccess = {
                    navController.popBackStack(Screen.Profile.route, false)
                }
            )
        }
    }
}