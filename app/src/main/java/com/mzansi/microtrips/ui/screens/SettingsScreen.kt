package com.mzansi.microtrips.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mzansi.microtrips.utils.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onBackClick: () -> Unit
) {
    val darkMode by preferencesManager.isDarkMode.collectAsState(initial = false)
    val dynamicColor by preferencesManager.isDynamicColor.collectAsState(initial = true)
    val largeText by preferencesManager.isLargeText.collectAsState(initial = false)

    val scope = CoroutineScope(Dispatchers.IO)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings ⚙️") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Dark Mode
            SettingItem(
                title = "Dark Mode",
                icon = Icons.Default.DarkMode,
                isChecked = darkMode,
                onCheckedChange = {
                    scope.launch {
                        preferencesManager.setDarkMode(it)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Large Text
            SettingItem(
                title = "Large Text",
                icon = Icons.Default.FormatSize,
                isChecked = largeText,
                onCheckedChange = {
                    scope.launch {
                        preferencesManager.setLargeText(it)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dynamic Color
            SettingItem(
                title = "Dynamic Color (Android 12+)",
                icon = Icons.Default.Palette,
                isChecked = dynamicColor,
                onCheckedChange = {
                    scope.launch {
                        preferencesManager.setDynamicColor(it)
                    }
                }
            )
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}