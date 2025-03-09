package com.splitsage.android.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SplitSage") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navigate to Add Expense */ }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Expense"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BalanceSummaryCard()
            Spacer(modifier = Modifier.height(16.dp))
            RecentActivitySection()
        }
    }
}

@Composable
fun BalanceSummaryCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$120.75",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You are owed $150.50 and you owe $29.75",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RecentActivitySection() {
    Text(
        text = "Recent Activity",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    // Dummy data for recent activities
    val recentActivities = listOf(
        Activity("Dinner at Italian Restaurant", "Alex paid $85.50", "Yesterday"),
        Activity("Groceries", "You paid $45.25", "2 days ago"),
        Activity("Movie Night", "Sarah paid $32.00", "Last week"),
        Activity("Utilities", "You paid $120.00", "Last week")
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recentActivities) { activity ->
            ActivityCard(activity)
        }
    }
}

@Composable
fun ActivityCard(activity: Activity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = activity.time,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

data class Activity(
    val title: String,
    val description: String,
    val time: String
)