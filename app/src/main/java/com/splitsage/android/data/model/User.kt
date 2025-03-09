package com.splitsage.android.data.model

import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a user in the SplitSage app.
 */
data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val profileImageUrl: String? = null,
    val defaultCurrency: String = "USD",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastActive: LocalDateTime = LocalDateTime.now(),
    val fcmToken: String? = null, // Firebase Cloud Messaging token for notifications
    val preferences: UserPreferences = UserPreferences()
)

/**
 * Represents a user's preferences.
 */
data class UserPreferences(
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val emailNotificationsEnabled: Boolean = true,
    val language: String = "en", // ISO 639-1 language code
)

/**
 * Represents a friendship between two users.
 */
data class Friendship(
    val id: String = UUID.randomUUID().toString(),
    val userId1: String,
    val userId2: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val status: FriendshipStatus = FriendshipStatus.PENDING
)

/**
 * Enum representing different friendship statuses.
 */
enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    BLOCKED
}