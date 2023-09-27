package i.herman.domain.people

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.user.Friend


class InMemoryPeopleCatalog(
    private val peopleForUserId: Map<String, List<Friend>>,
) : PeopleCatalog {

    override suspend fun loadPeopleFor(userId: String): List<Friend> {
        if (userId.isBlank()) throw ConnectionUnavailableException()
        return peopleForUserId[userId] ?: throw BackendException()
    }
}