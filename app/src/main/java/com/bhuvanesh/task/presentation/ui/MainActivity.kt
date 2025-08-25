package com.bhuvanesh.task.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.bhuvanesh.task.presentation.ui.navigation.BottomNavBar
import com.bhuvanesh.task.presentation.ui.navigation.TabNavigation
import com.bhuvanesh.task.presentation.viewmodel.HoldingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HoldingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .statusBarsPadding()
                        .fillMaxSize()
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomNavBar(
                                viewModel.bottomBarItems.collectAsState().value,
                                onItemClick = {
                                    navController.navigate(it)
                                }
                            )
                        },
                    ) { innerPadding ->
                        TabNavigation(Modifier.padding(innerPadding), navController, viewModel)
                    }
                }
            }
        }
    }
}