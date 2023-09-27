package i.herman.domain.people

import i.herman.domain.user.Friend

interface PeopleCatalog {
    suspend fun loadPeopleFor(userId: String): List<Friend>
}