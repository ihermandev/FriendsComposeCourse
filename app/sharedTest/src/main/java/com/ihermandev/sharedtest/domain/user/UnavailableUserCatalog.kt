package com.ihermandev.sharedtest.domain.user

import i.herman.domain.exceptions.BackendException
import i.herman.domain.user.User
import i.herman.domain.user.UserCatalog

class UnavailableUserCatalog : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
        throw BackendException()
    }

    override fun followedBy(userId: String): List<String> {
        throw BackendException()
    }
}