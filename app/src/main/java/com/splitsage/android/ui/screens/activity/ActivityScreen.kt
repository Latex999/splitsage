package com.splitsage.android.ui.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (dummyActivities.isEmpty()) {
                EmptyActivityState()
            } else {
                ActivityList(activities = dummyActivities)
            }
        }
    }
}

@Composable
fun EmptyActivityState() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Activity Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your recent expense and payment activity will show up here.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActivityList(activities: List<ActivityItem>) {
    // Group activities by date
    val groupedActivities = activities.groupBy { activity ->
        when {
            activity.timestamp.toLocalDate() == LocalDateTime.now().toLocalDate() -> "Today"
            activity.timestamp.toLocalDate() == LocalDateTime.now().minusDays(1).toLocalDate() -> "Yesterday"
            activity.timestamp.isAfter(LocalDateTime.now().minusDays(7)) -> "This Week"
            activity.timestamp.isAfter(LocalDateTime.now().minusDays(30)) -> "This Month"
            else -> "Earlier"
        }
    }

    LazyColumn {
        groupedActivities.forEach { (dateGroup, activitiesInGroup) ->
            item {
                Text(
                    text = dateGroup,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
            
            items(activitiesInGroup) { activity ->
                ActivityItemCard(activity = activity)
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                Divider()
            }
        }
    }
}

@Composable
fun ActivityItemCard(activity: ActivityItem) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(activity.iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = activity.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = activity.timestamp.format(DateTimeFormatter.ofPattern("h:mm a")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

enum class ActivityType {
    EXPENSE_ADDED,
    PAYMENT,
    SETTLED_UP
}

data class ActivityItem(
    val id: String,
    val type: ActivityType,
    val title: String,
    val description: String,
    val timestamp: LocalDateTime,
    val icon: ImageVector,
    val iconBackgroundColor: Color
)

// Dummy data
val dummyActivities = listOf(
    ActivityItem(
        id = "1",
        type = ActivityType.EXPENSE_ADDED,
        title = "Expense Added",
        description = "Alex added \"Dinner at Italian Restaurant\" ($120.00)",
        timestamp = LocalDateTime.now().minusHours(3),
        icon = Icons.Filled.Receipt,
        iconBackgroundColor = MaterialTheme.colorScheme.primary
    ),
    ActivityItem(
        id = "2",
        type = ActivityType.PAYMENT,
        title = "Payment Sent",
        description = "You paid Sarah $25.75",
        timestamp = LocalDateTime.now().minusDays(1),
        icon = Icons.Filled.MonetizationOn,
        iconBackgroundColor = Color(0xFF66BB6A)
    ),
    ActivityItem(
        id = "3",
        type = ActivityType.SETTLED_UP,
        title = "Settled Up",
        description = "You and Mike are now settled up",
        timestamp = LocalDateTime.now().minusDays(3),
        icon = Icons.Filled.Check,
        iconBackgroundColor = Color(0xFF42A5F5)
    ),
    ActivityItem(
        id = "4",
        type = ActivityType.EXPENSE_ADDED,
        title = "Expense Added",
        description = "You added \"Groceries\" ($85.75)",
        timestamp = LocalDateTime.now().minusDays(5),
        icon = Icons.Filled.Receipt,
        iconBackgroundColor = MaterialTheme.colorScheme.primary
    ),
    ActivityItem(
        id = "5",
        type = ActivityType.PAYMENT,
        title = "Payment Received",
        description = "John paid you $32.50",
        timestamp = LocalDateTime.now().minusDays(10),
        icon = Icons.Filled.MonetizationOn,
        iconBackgroundColor = Color(0xFF66BB6A)
    )
)