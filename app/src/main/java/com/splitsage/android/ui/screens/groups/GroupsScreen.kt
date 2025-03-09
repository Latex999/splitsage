package com.splitsage.android.ui.screens.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Groups") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navigate to Create Group */ }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create Group"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (dummyGroups.isEmpty()) {
                EmptyGroupsState()
            } else {
                GroupsList(dummyGroups, onGroupClick = {
                    // TODO: Navigate to group details
                })
            }
        }
    }
}

@Composable
fun EmptyGroupsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Group,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Groups Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create a group to start splitting expenses with friends, family, or roommates.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun GroupsList(groups: List<Group>, onGroupClick: (Group) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(groups) { group ->
            GroupCard(group = group, onClick = { onGroupClick(group) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCard(group: Group, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group avatar/icon
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                color = group.color
            ) {
                Text(
                    text = group.name.first().toString(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${group.members} members",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (group.balance > 0) {
                    Text(
                        text = "You are owed: $${group.balance}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (group.balance < 0) {
                    Text(
                        text = "You owe: $${-group.balance}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "Settled up",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "View Group"
                )
            }
        }
    }
}

data class Group(
    val id: String,
    val name: String,
    val members: Int,
    val balance: Double,
    val color: Color
)

// Dummy data
val dummyGroups = listOf(
    Group("1", "Roommates", 3, 85.50, Color(0xFF5C6BC0)),
    Group("2", "Trip to Hawaii", 5, -42.75, Color(0xFF26A69A)),
    Group("3", "Family", 4, 0.0, Color(0xFFEF5350)),
    Group("4", "Dinner Club", 6, 120.25, Color(0xFFFFB74D))
)