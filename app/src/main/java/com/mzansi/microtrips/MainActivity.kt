package com.mzansi.microtrips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mzansi.microtrips.data.repository.GemsRepository
import com.mzansi.microtrips.data.source.JsonSource
import com.mzansi.microtrips.ui.navigation.MzansiApp
import com.mzansi.microtrips.ui.theme.MzansiMicrotripsTheme
import com.mzansi.microtrips.utils.PreferencesManager

class MainActivity : ComponentActivity() {

    private val gemsRepository: GemsRepository by lazy {
        val jsonSource = JsonSource(this)
        GemsRepository(jsonSource)
    }

    private val preferencesManager: PreferencesManager by lazy {
        PreferencesManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkMode by preferencesManager.isDarkMode.collectAsState(initial = isSystemInDarkTheme())
            val isDynamicColor by preferencesManager.isDynamicColor.collectAsState(initial = true)

            MzansiMicrotripsTheme(
                darkTheme = isDarkMode,
                dynamicColor = isDynamicColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MzansiMicrotripsApp(gemsRepository, preferencesManager)
                }
            }
        }
    }
}

@Composable
fun MzansiMicrotripsApp(repository: GemsRepository, preferencesManager: PreferencesManager) {
    val navController = rememberNavController()

    MzansiApp(
        repository = repository,
        preferencesManager = preferencesManager,
        navController = navController
    )
}
