package com.example.weatherapp2026.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp2026.presentation.components.CCHomeSkeletonScreen
import com.example.weatherapp2026.presentation.components.CCLoadingView
import com.example.weatherapp2026.presentation.screens.forecast.CCForecastScreen
import com.example.weatherapp2026.presentation.screens.home.CCHomeScreen
import com.example.weatherapp2026.presentation.screens.home.CCHomeUiState
import com.example.weatherapp2026.presentation.screens.home.CCHomeViewModel

object CCDestinations {
    const val HOME = "home"
    const val FORECAST = "forecast"
}

@Composable
fun CCNavGraph(m_navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = m_navController,
        startDestination = CCDestinations.HOME
    ) {
        composable(CCDestinations.HOME) {
            CCHomeScreen(
                onNavigateToForecast = { m_navController.navigate(CCDestinations.FORECAST) }
            )
        }

        composable(CCDestinations.FORECAST) { m_backStackEntry ->
            // Share the ViewModel from the HOME back-stack entry so both screens
            // operate on the same data without re-fetching.
            val m_homeEntry = remember(m_backStackEntry) {
                m_navController.getBackStackEntry(CCDestinations.HOME)
            }
            val m_viewModel: CCHomeViewModel = hiltViewModel(m_homeEntry)
            val m_uiState by m_viewModel.m_uiState.collectAsStateWithLifecycle()

            when (val m_state = m_uiState) {
                is CCHomeUiState.Success -> CCForecastScreen(
                    m_state = m_state,
                    onBack = { m_navController.popBackStack() }
                )
                is CCHomeUiState.Loading -> CCHomeSkeletonScreen()
                is CCHomeUiState.Error -> CCLoadingView()
            }
        }
    }
}
