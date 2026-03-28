package com.mzansi.microtrips.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mzansi.microtrips.data.repository.GemsRepository
import com.mzansi.microtrips.ui.screens.DetailScreen
import com.mzansi.microtrips.ui.screens.ExploreScreen
import com.mzansi.microtrips.ui.screens.FavouritesScreen
import com.mzansi.microtrips.ui.screens.SettingsScreen
import com.mzansi.microtrips.utils.PreferencesManager

sealed class Screen(val route: String) {
    data object Explore : Screen("explore")
    data object Favourites : Screen("favourites")
    data object Settings : Screen("settings")
    data object Detail : Screen("detail/{gemId}") {
        fun passId(gemId: Int): String = "detail/$gemId"
    }
}

@Composable
fun AppNavHost(
    repository: GemsRepository,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val gems by repository.gems.collectAsState()
    val favorites by repository.favorites.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Explore.route,
        modifier = modifier
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
