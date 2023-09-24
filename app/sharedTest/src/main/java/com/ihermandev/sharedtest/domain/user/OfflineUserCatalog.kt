package com.ihermandev.sharedtest.domain.user

import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.user.User
import i.herman.domain.user.UserCatalog

class OfflineUserCatalog : UserCatalog {

    override suspend fun createUser(
        email: String,
        password: String,
        about: String
    ): User {
        throw ConnectionUnavailableException()
    }

    override fun followedBy(userId: String): List<String> {
        throw ConnectionUnavailableException()
    }
}