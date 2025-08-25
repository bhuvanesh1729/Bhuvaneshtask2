package com.bhuvanesh.task.presentation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.bhuvanesh.task.data.uidata.BottomNavigationItem

// Data class to represent each navigation item


@Composable
fun BottomNavBar(
    items: List<BottomNavigationItem>,
    onItemClick: (String) -> Unit,
) {
    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }
    NavigationBar(containerColor = Color.LightGray) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onItemClick(item.title)
                },
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title
                    )
                },
            )
        }
    }
}

// --- Placeholder Content for Demonstration ---
@Composable
fun HomeScreenContent() {
    Text(text = "Portfolio Screen")
}

