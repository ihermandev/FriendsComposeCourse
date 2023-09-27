package i.herman.people

import i.herman.InstantTaskExecutorExtension
import i.herman.domain.people.InMemoryPeopleCatalog
import i.herman.domain.people.PeopleRepository
import i.herman.domain.user.Friend
import i.herman.domain.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailPeopleLoadingTest {

    @Test
    fun backendError() {
        val viewModel = PeopleViewModel(PeopleRepository(InMemoryPeopleCatalog(
            mapOf(
                "annaId" to listOf(Friend(User("tomId", "", ""), isFollowee = false)),
                "lucyId" to listOf(
                    Friend(User("annaId", "", ""), isFollowee = true),
                    Friend(User("saraId", "", ""), isFollowee = false),
                    Friend(User("tomId", "", ""), isFollowee = false)
                ),
                "saraId" to emptyList()
            )
        )
        ))

        viewModel.loadPeople("jovId")

        assertEquals(PeopleState.BackendError, viewModel.peopleState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = PeopleViewModel(PeopleRepository(InMemoryPeopleCatalog(
            mapOf(
                "annaId" to listOf(Friend(User("tomId", "", ""), isFollowee = false)),
                "lucyId" to listOf(
                    Friend(User("annaId", "", ""), isFollowee = true),
                    Friend(User("saraId", "", ""), isFollowee = false),
                    Friend(User("tomId", "", ""), isFollowee = false)
                ),
                "saraId" to emptyList()
            )
        )
        ))

        viewModel.loadPeople("")

        assertEquals(PeopleState.Offline, viewModel.peopleState.value)
    }
}