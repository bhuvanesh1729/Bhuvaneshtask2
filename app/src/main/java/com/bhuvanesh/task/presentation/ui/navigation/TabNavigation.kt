package com.bhuvanesh.task.presentation.ui.navigation

import ApiDataHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bhuvanesh.task.presentation.ui.screens.PortfolioScreen
import com.bhuvanesh.task.presentation.viewmodel.HoldingsViewModel

@Composable
fun TabNavigation(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: HoldingsViewModel
) {
    NavHost(modifier = modifier, navController = navController, startDestination = "Portfolio") {
        composable("Portfolio") {
            ApiDataHandler(
                Modifier.fillMaxSize(),
                uiState = viewModel.uiData.collectAsStateWithLifecycle().value,
                onRefresh = {
                    viewModel.initialiseData()
                }) { modifier, data ->
                PortfolioScreen(Modifier.fillMaxSize(), data)
            }
        }
        composable("home") {
            HomeScreenContent()
        }
    }
}