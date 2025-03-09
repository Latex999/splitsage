package com.splitsage.android.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsage.android.data.model.Expense
import com.splitsage.android.data.model.User
import com.splitsage.android.data.repository.ExpenseRepository
import com.splitsage.android.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the Expenses screen.
 */
@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow<ExpensesUiState>(ExpensesUiState.Loading)
    val uiState: StateFlow<ExpensesUiState> = _uiState
    
    // Current filter
    private val _currentFilter = MutableStateFlow(ExpenseFilter.ALL)
    val currentFilter: StateFlow<ExpenseFilter> = _currentFilter
    
    // Current user
    private val _currentUser = MutableStateFlow<User?>(null)
    
    init {
        loadCurrentUser()
        loadExpenses()
    }
    
    /**
     * Load the current user.
     */
    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val user = userRepository.getCurrentUser()
                _currentUser.value = user
            } catch (e: Exception) {
                Timber.e(e, "Error loading current user")
            }
        }
    }
    
    /**
     * Load expenses based on the current filter.
     */
    private fun loadExpenses() {
        viewModelScope.launch {
            val currentUser = _currentUser.value
            
            if (currentUser == null) {
                _uiState.value = ExpensesUiState.Error("User not logged in")
                return@launch
            }
            
            // Combine current filter with expenses flow
            combine(
                _currentFilter,
                expenseRepository.getAllExpenses()
            ) { filter, expenses ->
                // Apply filter
                val filteredExpenses = when (filter) {
                    ExpenseFilter.ALL -> expenses
                    ExpenseFilter.I_OWE -> expenses.filter { expense ->
                        expense.paidBy != currentUser.id && expense.splits.any { 
                            it.userId == currentUser.id && !it.paid 
                        }
                    }
                    ExpenseFilter.IM_OWED -> expenses.filter { expense ->
                        expense.paidBy == currentUser.id && expense.splits.any { 
                            !it.paid 
                        }
                    }
                }
                
                if (filteredExpenses.isEmpty()) {
                    ExpensesUiState.Empty
                } else {
                    ExpensesUiState.Success(filteredExpenses)
                }
            }.catch { e ->
                Timber.e(e, "Error loading expenses")
                _uiState.value = ExpensesUiState.Error(e.message ?: "Unknown error")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    /**
     * Set the current expense filter.
     */
    fun setFilter(filter: ExpenseFilter) {
        _currentFilter.value = filter
    }
    
    /**
     * Delete an expense.
     */
    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                val success = expenseRepository.deleteExpense(expenseId)
                if (!success) {
                    Timber.e("Failed to delete expense: $expenseId")
                }
            } catch (e: Exception) {
                Timber.e(e, "Error deleting expense")
            }
        }
    }
}

/**
 * UI state for the Expenses screen.
 */
sealed class ExpensesUiState {
    object Loading : ExpensesUiState()
    object Empty : ExpensesUiState()
    data class Success(val expenses: List<Expense>) : ExpensesUiState()
    data class Error(val message: String) : ExpensesUiState()
}

/**
 * Filters for expenses.
 */
enum class ExpenseFilter {
    ALL,
    I_OWE,
    IM_OWED
}