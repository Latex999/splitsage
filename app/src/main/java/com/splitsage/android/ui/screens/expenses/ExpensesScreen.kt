package com.splitsage.android.ui.screens.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.splitsage.android.data.model.Expense
import com.splitsage.android.data.model.ExpenseCategory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    navController: NavController,
    viewModel: ExpensesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    
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
            ExpenseFilters(
                currentFilter = currentFilter,
                onFilterSelected = { viewModel.setFilter(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (uiState) {
                is ExpensesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ExpensesUiState.Empty -> {
                    EmptyExpensesState()
                }
                is ExpensesUiState.Success -> {
                    val expenses = (uiState as ExpensesUiState.Success).expenses
                    ExpensesList(
                        expenses = expenses,
                        onExpenseClick = { /* TODO: Navigate to expense details */ },
                        onExpenseDelete = { viewModel.deleteExpense(it) }
                    )
                }
                is ExpensesUiState.Error -> {
                    val errorMessage = (uiState as ExpensesUiState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: $errorMessage",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseFilters(
    currentFilter: ExpenseFilter,
    onFilterSelected: (ExpenseFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (currentFilter) {
                ExpenseFilter.ALL -> "All Expenses"
                ExpenseFilter.I_OWE -> "I Owe"
                ExpenseFilter.IM_OWED -> "I'm Owed"
            },
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
                        onFilterSelected(ExpenseFilter.ALL)
                        showMenu = false 
                    }
                )
                DropdownMenuItem(
                    text = { Text("I owe") },
                    onClick = { 
                        onFilterSelected(ExpenseFilter.I_OWE)
                        showMenu = false 
                    }
                )
                DropdownMenuItem(
                    text = { Text("I'm owed") },
                    onClick = { 
                        onFilterSelected(ExpenseFilter.IM_OWED)
                        showMenu = false 
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyExpensesState() {
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
}

@Composable
fun ExpensesList(
    expenses: List<Expense>,
    onExpenseClick: (Expense) -> Unit,
    onExpenseDelete: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(expenses) { expense ->
            ExpenseCard(
                expense = expense,
                onClick = { onExpenseClick(expense) },
                onDelete = { onExpenseDelete(expense.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCard(
    expense: Expense,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Expense category icon
            Icon(
                imageVector = getCategoryIcon(expense.category),
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
            
            // TODO: Show amount owed or get back based on current user
        }
    }
}

// Helper function to map category to icon
@Composable
fun getCategoryIcon(category: ExpenseCategory): ImageVector {
    // TODO: Map different categories to different icons
    return Icons.Filled.Restaurant
}