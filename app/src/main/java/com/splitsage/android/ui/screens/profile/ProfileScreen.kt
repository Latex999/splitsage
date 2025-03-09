package com.splitsage.android.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = { /* TODO: Edit profile */ }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Profile"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileHeader()
            }
            
            item {
                SettingsSection(
                    title = "Account",
                    settings = listOf(
                        Setting(
                            title = "Personal Information",
                            icon = Icons.Filled.Person,
                            onClick = { /* TODO: Navigate to personal info */ }
                        ),
                        Setting(
                            title = "Privacy & Security",
                            icon = Icons.Filled.Lock,
                            onClick = { /* TODO: Navigate to privacy settings */ }
                        ),
                        Setting(
                            title = "Notifications",
                            icon = Icons.Filled.Notifications,
                            onClick = { /* TODO: Navigate to notification settings */ }
                        )
                    )
                )
            }
            
            item {
                SettingsSection(
                    title = "Preferences",
                    settings = listOf(
                        Setting(
                            title = "Dark Mode",
                            icon = Icons.Filled.DarkMode,
                            toggle = true,
                            onClick = { /* TODO: Toggle dark mode */ }
                        ),
                        Setting(
                            title = "Language",
                            icon = Icons.Filled.Language,
                            description = "English",
                            onClick = { /* TODO: Navigate to language settings */ }
                        )
                    )
                )
            }
            
            item {
                SettingsSection(
                    title = "Support",
                    settings = listOf(
                        Setting(
                            title = "Help & Support",
                            icon = Icons.Filled.Help,
                            onClick = { /* TODO: Navigate to help page */ }
                        ),
                        Setting(
                            title = "About SplitSage",
                            icon = Icons.Filled.AccountBox,
                            onClick = { /* TODO: Navigate to about page */ }
                        )
                    )
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Setting(
                    title = "Sign Out",
                    icon = Icons.Filled.ExitToApp,
                    textColor = MaterialTheme.colorScheme.error,
                    onClick = { /* TODO: Sign out */ }
                )
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile avatar
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(68.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Alex Johnson",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "alex.johnson@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    settings: List<Setting>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp),
            fontWeight = FontWeight.Bold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                settings.forEachIndexed { index, setting ->
                    Setting(
                        title = setting.title,
                        description = setting.description,
                        icon = setting.icon,
                        toggle = setting.toggle,
                        textColor = setting.textColor,
                        onClick = setting.onClick
                    )
                    
                    if (index < settings.size - 1) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Setting(
    title: String,
    description: String? = null,
    icon: ImageVector,
    toggle: Boolean = false,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    var isToggled by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = textColor
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        if (toggle) {
            Switch(
                checked = isToggled,
                onCheckedChange = {
                    isToggled = it
                    onClick()
                }
            )
        } else {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class Setting(
    val title: String,
    val description: String? = null,
    val icon: ImageVector,
    val toggle: Boolean = false,
    val textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    val onClick: () -> Unit
)