package com.splitsage.android.data.repository

import com.splitsage.android.data.model.Friendship
import com.splitsage.android.data.model.FriendshipStatus
import com.splitsage.android.data.model.User
import com.splitsage.android.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

/**
 * Repository interface for managing users.
 */
interface UserRepository {
    /**
     * Get the current logged-in user.
     */
    suspend fun getCurrentUser(): User?
    
    /**
     * Get user by ID.
     */
    suspend fun getUserById(userId: String): User?
    
    /**
     * Get users by email.
     */
    suspend fun getUserByEmail(email: String): User?
    
    /**
     * Create a new user.
     */
    suspend fun createUser(user: User): String
    
    /**
     * Update an existing user.
     */
    suspend fun updateUser(user: User): Boolean
    
    /**
     * Update user preferences.
     */
    suspend fun updateUserPreferences(userId: String, preferences: UserPreferences): Boolean
    
    /**
     * Get all friendships for a user.
     */
    fun getUserFriendships(userId: String): Flow<List<Friendship>>
    
    /**
     * Get all friends for a user (accepted friendships only).
     */
    fun getUserFriends(userId: String): Flow<List<User>>
    
    /**
     * Create a friendship request.
     */
    suspend fun createFriendship(userId1: String, userId2: String): String
    
    /**
     * Update a friendship status.
     */
    suspend fun updateFriendshipStatus(friendshipId: String, status: FriendshipStatus): Boolean
    
    /**
     * Search for users by name or email.
     */
    suspend fun searchUsers(query: String): List<User>
}

/**
 * In-memory implementation of the UserRepository for development and testing.
 */
class InMemoryUserRepository : UserRepository {
    // In-memory storage
    private val users = mutableListOf<User>()
    private val friendships = mutableListOf<Friendship>()
    
    // Current user ID (simulates authentication)
    private var currentUserId: String? = null
    
    // Flow controllers
    private val userFriendshipsFlow = MutableStateFlow<List<Friendship>>(emptyList())
    
    override suspend fun getCurrentUser(): User? {
        return currentUserId?.let { getUserById(it) }
    }
    
    override suspend fun getUserById(userId: String): User? {
        return users.find { it.id == userId }
    }
    
    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }
    
    override suspend fun createUser(user: User): String {
        // Check if user with email already exists
        if (users.any { it.email == user.email }) {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }
        
        users.add(user)
        
        // If no current user is set, set this as the current user
        if (currentUserId == null) {
            currentUserId = user.id
        }
        
        return user.id
    }
    
    override suspend fun updateUser(user: User): Boolean {
        val index = users.indexOfFirst { it.id == user.id }
        if (index == -1) return false
        
        users[index] = user
        return true
    }
    
    override suspend fun updateUserPreferences(userId: String, preferences: UserPreferences): Boolean {
        val index = users.indexOfFirst { it.id == userId }
        if (index == -1) return false
        
        val user = users[index]
        users[index] = user.copy(preferences = preferences)
        return true
    }
    
    override fun getUserFriendships(userId: String): Flow<List<Friendship>> {
        return flow {
            emit(friendships.filter { it.userId1 == userId || it.userId2 == userId })
        }
    }
    
    override fun getUserFriends(userId: String): Flow<List<User>> {
        return flow {
            // Get all accepted friendships involving the user
            val userFriendships = friendships.filter { 
                (it.userId1 == userId || it.userId2 == userId) && 
                        it.status == FriendshipStatus.ACCEPTED 
            }
            
            // Get the IDs of the friends
            val friendIds = userFriendships.map { 
                if (it.userId1 == userId) it.userId2 else it.userId1 
            }
            
            // Get the User objects
            val friends = users.filter { it.id in friendIds }
            emit(friends)
        }
    }
    
    override suspend fun createFriendship(userId1: String, userId2: String): String {
        // Check if users exist
        val user1 = getUserById(userId1)
        val user2 = getUserById(userId2)
        
        if (user1 == null || user2 == null) {
            throw IllegalArgumentException("Both users must exist")
        }
        
        // Check if friendship already exists
        val existingFriendship = friendships.find {
            (it.userId1 == userId1 && it.userId2 == userId2) ||
                    (it.userId1 == userId2 && it.userId2 == userId1)
        }
        
        if (existingFriendship != null) {
            throw IllegalArgumentException("Friendship already exists")
        }
        
        // Create new friendship
        val friendship = Friendship(
            userId1 = userId1,
            userId2 = userId2,
            status = FriendshipStatus.PENDING
        )
        
        friendships.add(friendship)
        userFriendshipsFlow.value = friendships.toList()
        
        return friendship.id
    }
    
    override suspend fun updateFriendshipStatus(friendshipId: String, status: FriendshipStatus): Boolean {
        val index = friendships.indexOfFirst { it.id == friendshipId }
        if (index == -1) return false
        
        friendships[index] = friendships[index].copy(status = status)
        userFriendshipsFlow.value = friendships.toList()
        
        return true
    }
    
    override suspend fun searchUsers(query: String): List<User> {
        val lowercaseQuery = query.lowercase()
        return users.filter { user ->
            user.name.lowercase().contains(lowercaseQuery) ||
                    user.email.lowercase().contains(lowercaseQuery)
        }
    }
    
    // Helper method to set the current user (for testing)
    fun setCurrentUser(userId: String) {
        currentUserId = userId
    }
}