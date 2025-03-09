package com.splitsage.android.ui.screens.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") }
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
                .padding(16.dp)
        ) {
            ExpenseFilters()
            Spacer(modifier = Modifier.height(16.dp))
            ExpensesList(expenses = dummyExpenses)
        }
    }
}

@Composable
fun ExpenseFilters() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "All Expenses",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Row {
            IconButton(onClick = { /* TODO: Filter by date */ }) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = "Filter by date"
                )
            }
            
            var showMenu by remember { mutableStateOf(false) }
            
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options"
                )
            }
            
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All expenses") },
                    onClick = { 
                        // TODO: Filter expenses
                        showMenu = false 
                    }
                )
                DropdownMenuItem(
                    text = { Text("I owe") },
                    onClick = { 
                        // TODO: Filter expenses where user owes money
                        showMenu = false 
                    }
                )
                DropdownMenuItem(
                    text = { Text("I'm owed") },
                    onClick = { 
                        // TODO: Filter expenses where user is owed money
                        showMenu = false 
                    }
                )
            }
        }
    }
}

@Composable
fun ExpensesList(expenses: List<Expense>) {
    if (expenses.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No expenses yet",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add your first expense by tapping the + button",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(expenses) { expense ->
                ExpenseCard(expense = expense)
            }
        }
    }
}

@Composable
fun ExpenseCard(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Expense category icon
            Icon(
                imageVector = expense.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = expense.date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${expense.paidBy} paid $${expense.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                val amountText = if (expense.paidBy == "You") {
                    "You get back"
                } else {
                    "Your share"
                }
                
                val amount = if (expense.paidBy == "You") {
                    expense.amount - expense.yourShare
                } else {
                    expense.yourShare
                }
                
                Text(
                    text = amountText,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "$${String.format("%.2f", amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (expense.paidBy == "You") 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val date: LocalDate,
    val paidBy: String,
    val yourShare: Double,
    val icon: ImageVector
)

// Dummy data
val dummyExpenses = listOf(
    Expense(
        id = "1",
        title = "Dinner at Italian Restaurant",
        amount = 120.0,
        date = LocalDate.now().minusDays(2),
        paidBy = "Alex",
        yourShare = 40.0,
        icon = Icons.Filled.Restaurant
    ),
    Expense(
        id = "2",
        title = "Groceries",
        amount = 85.75,
        date = LocalDate.now().minusDays(5),
        paidBy = "You",
        yourShare = 28.58,
        icon = Icons.Filled.Restaurant
    ),
    Expense(
        id = "3",
        title = "Movie Night",
        amount = 48.0,
        date = LocalDate.now().minusWeeks(1),
        paidBy = "Sarah",
        yourShare = 12.0,
        icon = Icons.Filled.Restaurant
    ),
    Expense(
        id = "4",
        title = "Utilities - March",
        amount = 145.50,
        date = LocalDate.now().minusWeeks(2),
        paidBy = "You",
        yourShare = 48.50,
        icon = Icons.Filled.Restaurant
    )
)