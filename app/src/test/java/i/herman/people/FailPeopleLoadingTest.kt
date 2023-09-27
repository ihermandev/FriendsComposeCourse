package i.herman.people

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.people.PeopleCatalog
import i.herman.domain.people.PeopleRepository
import i.herman.domain.user.Friend
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class FailPeopleLoadingTest {

    @Test
    fun backendError() {
        val viewModel = PeopleViewModel(PeopleRepository(UnavailablePeopleCatalog()), TestDispatchers())

        viewModel.loadPeople(":irrelevant:")

        assertEquals(PeopleState.BackendError, viewModel.peopleState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = PeopleViewModel(PeopleRepository(OfflinePeopleCatalog()), TestDispatchers())

        viewModel.loadPeople(":irrelevant:")

        assertEquals(PeopleState.Offline, viewModel.peopleState.value)
    }

    private class UnavailablePeopleCatalog : PeopleCatalog {

        override suspend fun loadPeopleFor(userId: String): List<Friend> {
            throw BackendException()
        }
    }

    private class OfflinePeopleCatalog : PeopleCatalog {

        override suspend fun loadPeopleFor(userId: String): List<Friend> {
            throw ConnectionUnavailableException()
        }
    }
}