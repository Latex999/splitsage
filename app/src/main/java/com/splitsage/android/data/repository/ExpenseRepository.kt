package com.splitsage.android.data.repository

import com.splitsage.android.data.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Repository interface for managing expenses.
 */
interface ExpenseRepository {
    /**
     * Get all expenses as a Flow.
     */
    fun getAllExpenses(): Flow<List<Expense>>
    
    /**
     * Get all expenses for a specific group.
     */
    fun getExpensesByGroup(groupId: String): Flow<List<Expense>>
    
    /**
     * Get all expenses paid by a specific user.
     */
    fun getExpensesByPayer(userId: String): Flow<List<Expense>>
    
    /**
     * Get expenses where a user is involved (either paid or owes).
     */
    fun getExpensesInvolvingUser(userId: String): Flow<List<Expense>>
    
    /**
     * Get expenses within a date range.
     */
    fun getExpensesByDateRange(from: LocalDateTime, to: LocalDateTime): Flow<List<Expense>>
    
    /**
     * Get a single expense by ID.
     */
    suspend fun getExpenseById(expenseId: String): Expense?
    
    /**
     * Add a new expense.
     */
    suspend fun addExpense(expense: Expense): String
    
    /**
     * Update an existing expense.
     */
    suspend fun updateExpense(expense: Expense): Boolean
    
    /**
     * Delete an expense.
     */
    suspend fun deleteExpense(expenseId: String): Boolean
}

/**
 * In-memory implementation of the ExpenseRepository for development and testing.
 */
class InMemoryExpenseRepository : ExpenseRepository {
    // In-memory storage
    private val expenses = mutableListOf<Expense>()
    
    // Flow controllers
    private val allExpensesFlow = kotlinx.coroutines.flow.MutableStateFlow<List<Expense>>(emptyList())
    
    override fun getAllExpenses(): Flow<List<Expense>> {
        return allExpensesFlow
    }
    
    override fun getExpensesByGroup(groupId: String): Flow<List<Expense>> {
        return kotlinx.coroutines.flow.flow {
            emit(expenses.filter { it.groupId == groupId })
        }
    }
    
    override fun getExpensesByPayer(userId: String): Flow<List<Expense>> {
        return kotlinx.coroutines.flow.flow {
            emit(expenses.filter { it.paidBy == userId })
        }
    }
    
    override fun getExpensesInvolvingUser(userId: String): Flow<List<Expense>> {
        return kotlinx.coroutines.flow.flow {
            emit(expenses.filter { expense ->
                expense.paidBy == userId || expense.splits.any { it.userId == userId }
            })
        }
    }
    
    override fun getExpensesByDateRange(from: LocalDateTime, to: LocalDateTime): Flow<List<Expense>> {
        return kotlinx.coroutines.flow.flow {
            emit(expenses.filter { it.date in from..to })
        }
    }
    
    override suspend fun getExpenseById(expenseId: String): Expense? {
        return expenses.find { it.id == expenseId }
    }
    
    override suspend fun addExpense(expense: Expense): String {
        expenses.add(expense)
        updateFlows()
        return expense.id
    }
    
    override suspend fun updateExpense(expense: Expense): Boolean {
        val index = expenses.indexOfFirst { it.id == expense.id }
        if (index == -1) return false
        
        expenses[index] = expense
        updateFlows()
        return true
    }
    
    override suspend fun deleteExpense(expenseId: String): Boolean {
        val removed = expenses.removeIf { it.id == expenseId }
        if (removed) {
            updateFlows()
        }
        return removed
    }
    
    // Helper method to update all flows with the latest data
    private fun updateFlows() {
        allExpensesFlow.value = expenses.toList()
    }
}