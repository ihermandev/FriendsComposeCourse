package i.herman.domain.user

import i.herman.domain.friends.ToggleFollowing

interface UserCatalog {

    suspend fun createUser(
        email: String,
        password: String,
        about: String
    ): User


    suspend fun toggleFollowing(userId: String, followerId: String): ToggleFollowing

    suspend fun followedBy(userId: String): List<String>

    suspend fun loadFriendsFor(userId: String): List<Friend>
}