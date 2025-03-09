package com.splitsage.android.data.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a group of users who share expenses.
 */
data class Group(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val createdBy: String, // User ID of the creator
    val members: List<GroupMember> = emptyList(),
    val currency: String = "USD", // Default currency
    val imageUrl: String? = null, // URL or path to group image
    val isArchived: Boolean = false
)

/**
 * Represents a member in a group.
 */
data class GroupMember(
    val userId: String,
    val joinedAt: LocalDateTime = LocalDateTime.now(),
    val role: GroupRole = GroupRole.MEMBER
)

/**
 * Enum representing different roles a user can have in a group.
 */
enum class GroupRole {
    ADMIN,
    MEMBER
}