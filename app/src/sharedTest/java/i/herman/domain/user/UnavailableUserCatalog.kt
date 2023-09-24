package i.herman.domain.user

import i.herman.domain.exceptions.BackendException

class UnavailableUserCatalog : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
        throw BackendException()
    }

    override fun followedBy(userId: String): List<String> {
        throw BackendException()
    }
}