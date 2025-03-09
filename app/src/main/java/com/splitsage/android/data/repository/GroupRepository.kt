package com.splitsage.android.data.repository

import com.splitsage.android.data.model.Group
import com.splitsage.android.data.model.GroupMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

/**
 * Repository interface for managing groups.
 */
interface GroupRepository {
    /**
     * Get all groups as a Flow.
     */
    fun getAllGroups(): Flow<List<Group>>
    
    /**
     * Get groups where a specific user is a member.
     */
    fun getGroupsByUser(userId: String): Flow<List<Group>>
    
    /**
     * Get a single group by ID.
     */
    suspend fun getGroupById(groupId: String): Group?
    
    /**
     * Create a new group.
     */
    suspend fun createGroup(group: Group): String
    
    /**
     * Update an existing group.
     */
    suspend fun updateGroup(group: Group): Boolean
    
    /**
     * Delete a group.
     */
    suspend fun deleteGroup(groupId: String): Boolean
    
    /**
     * Add a user to a group.
     */
    suspend fun addUserToGroup(groupId: String, groupMember: GroupMember): Boolean
    
    /**
     * Remove a user from a group.
     */
    suspend fun removeUserFromGroup(groupId: String, userId: String): Boolean
}

/**
 * In-memory implementation of the GroupRepository for development and testing.
 */
class InMemoryGroupRepository : GroupRepository {
    // In-memory storage
    private val groups = mutableListOf<Group>()
    
    // Flow controllers
    private val allGroupsFlow = MutableStateFlow<List<Group>>(emptyList())
    
    override fun getAllGroups(): Flow<List<Group>> {
        return allGroupsFlow
    }
    
    override fun getGroupsByUser(userId: String): Flow<List<Group>> {
        return flow {
            emit(groups.filter { group -> 
                group.members.any { it.userId == userId } 
            })
        }
    }
    
    override suspend fun getGroupById(groupId: String): Group? {
        return groups.find { it.id == groupId }
    }
    
    override suspend fun createGroup(group: Group): String {
        groups.add(group)
        updateFlows()
        return group.id
    }
    
    override suspend fun updateGroup(group: Group): Boolean {
        val index = groups.indexOfFirst { it.id == group.id }
        if (index == -1) return false
        
        groups[index] = group
        updateFlows()
        return true
    }
    
    override suspend fun deleteGroup(groupId: String): Boolean {
        val removed = groups.removeIf { it.id == groupId }
        if (removed) {
            updateFlows()
        }
        return removed
    }
    
    override suspend fun addUserToGroup(groupId: String, groupMember: GroupMember): Boolean {
        val index = groups.indexOfFirst { it.id == groupId }
        if (index == -1) return false
        
        val group = groups[index]
        // Don't add if user already exists in the group
        if (group.members.any { it.userId == groupMember.userId }) {
            return false
        }
        
        // Create a new group with the updated members list
        val updatedGroup = group.copy(
            members = group.members + groupMember
        )
        
        groups[index] = updatedGroup
        updateFlows()
        return true
    }
    
    override suspend fun removeUserFromGroup(groupId: String, userId: String): Boolean {
        val index = groups.indexOfFirst { it.id == groupId }
        if (index == -1) return false
        
        val group = groups[index]
        // Check if user exists in the group
        if (!group.members.any { it.userId == userId }) {
            return false
        }
        
        // Create a new group with the updated members list
        val updatedGroup = group.copy(
            members = group.members.filter { it.userId != userId }
        )
        
        groups[index] = updatedGroup
        updateFlows()
        return true
    }
    
    // Helper method to update all flows with the latest data
    private fun updateFlows() {
        allGroupsFlow.value = groups.toList()
    }
}