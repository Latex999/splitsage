package com.splitsage.android.data.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents an expense in the SplitSage app.
 */
data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val amount: Double,
    val date: LocalDateTime = LocalDateTime.now(),
    val paidBy: String, // User ID of the person who paid
    val groupId: String? = null, // Can be null if it's a personal expense
    val category: ExpenseCategory = ExpenseCategory.GENERAL,
    val splits: List<ExpenseSplit> = emptyList(),
    val receipt: String? = null, // URL or path to receipt image
)

/**
 * Represents how an expense is split among users.
 */
data class ExpenseSplit(
    val userId: String,
    val amount: Double,
    val paid: Boolean = false,
    val paidDate: LocalDateTime? = null
)

/**
 * Enum representing different expense categories.
 */
enum class ExpenseCategory {
    GENERAL,
    FOOD,
    TRANSPORT,
    SHOPPING,
    ENTERTAINMENT,
    HOUSING,
    UTILITIES,
    TRAVEL,
    PERSONAL,
    OTHER
}