package com.ihermandev.sharedtest.domain.user

import i.herman.domain.exceptions.BackendException
import i.herman.domain.friends.ToggleFollowing
import i.herman.domain.user.Friend
import i.herman.domain.user.User
import i.herman.domain.user.UserCatalog

class UnavailableUserCatalog : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
        throw BackendException()
    }

    override fun toggleFollowing(userId: String, followerId: String): ToggleFollowing {
        throw BackendException()
    }

    override suspend fun followedBy(userId: String): List<String> {
        throw BackendException()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        throw BackendException()
    }
}