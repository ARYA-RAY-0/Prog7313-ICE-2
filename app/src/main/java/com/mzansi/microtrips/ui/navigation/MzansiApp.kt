package com.mzansi.microtrips.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mzansi.microtrips.data.repository.GemsRepository
import com.mzansi.microtrips.ui.screens.DetailScreen
import com.mzansi.microtrips.ui.screens.ExploreScreen
import com.mzansi.microtrips.ui.screens.FavouritesScreen
import com.mzansi.microtrips.ui.screens.SettingsScreen
import com.mzansi.microtrips.utils.PreferencesManager
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MzansiApp(
    repository: GemsRepository,
    preferencesManager: PreferencesManager,
    navController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val gems by repository.gems.collectAsState()
    val favorites by repository.favorites.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Mzansi MicroTrips",
                    modifier = Modifier.padding(16.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Explore") },
                    selected = currentRoute == Screen.Explore.route,
                    onClick = {
                        navController.navigate(Screen.Explore.route) {
                            popUpTo(Screen.Explore.route) { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text("Favourites") },
                    selected = currentRoute == Screen.Favourites.route,
                    onClick = {
                        navController.navigate(Screen.Favourites.route)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = currentRoute == Screen.Settings.route,
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mzansi MicroTrips") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Explore.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Explore.route) {
                    ExploreScreen(
                        gems = gems,
                        favorites = favorites,
                        onToggleFavorite = { repository.toggleFavorite(it) },
                        onGemClick = { gemId ->
                            navController.navigate(Screen.Detail.passId(gemId))
                        }
                    )
                }

                composable(Screen.Favourites.route) {
                    FavouritesScreen(
                        gems = gems.filter { favorites.contains(it.id) },
                        favorites = favorites,
                        onToggleFavorite = { repository.toggleFavorite(it) },
                        onGemClick = { gemId ->
                            navController.navigate(Screen.Detail.passId(gemId))
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        preferencesManager = preferencesManager,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Screen.Detail.route) { backStackEntry ->
                    val gemId = backStackEntry.arguments?.getString("gemId")?.toIntOrNull() ?: 0
                    val gem = gems.find { it.id == gemId }

                    if (gem != null) {
                        DetailScreen(
                            gem = gem,
                            isFavorite = favorites.contains(gem.id),
                            onToggleFavorite = { repository.toggleFavorite(gem.id) },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
