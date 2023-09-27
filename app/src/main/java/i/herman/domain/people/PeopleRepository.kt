package i.herman.domain.people

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.people.PeopleState


class PeopleRepository(
    private val peopleCatalog: InMemoryPeopleCatalog
) {

    fun loadPeopleFor(userId: String): PeopleState {
        return try {
            val peopleForUserId = peopleCatalog.loadPeopleFor(userId)
            PeopleState.Loaded(peopleForUserId)
        } catch (backendException: BackendException) {
            PeopleState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            PeopleState.Offline
        }
    }
}