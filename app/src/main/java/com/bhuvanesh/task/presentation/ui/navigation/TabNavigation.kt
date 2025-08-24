package com.bhuvanesh.task.presentation.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bhuvanesh.task.presentation.ui.components.ApiFetcher
import com.bhuvanesh.task.presentation.ui.screens.PortfolioScreen
import com.bhuvanesh.task.presentation.viewmodel.HoldingsViewModel

@Composable
fun TabNavigation(modifier: Modifier, navController: NavHostController, viewModel: HoldingsViewModel) {
    NavHost(modifier = modifier, navController = navController, startDestination = "Portfolio") {
        composable("Portfolio") {
            ApiFetcher(Modifier.fillMaxSize(), viewModel) { modifier, data ->
                PortfolioScreen(Modifier.fillMaxSize(), data)
            }
        }
        composable("home"){
            HomeScreenContent()
        }
    }
}