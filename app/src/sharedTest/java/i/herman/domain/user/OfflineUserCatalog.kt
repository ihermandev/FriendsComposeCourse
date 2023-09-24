package i.herman.domain.user

import i.herman.domain.exceptions.ConnectionUnavailableException

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