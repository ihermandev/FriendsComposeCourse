package i.herman.people

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.people.InMemoryPeopleCatalog
import i.herman.domain.people.PeopleRepository
import i.herman.domain.user.Friend
import i.herman.infrastructure.builder.UserBuilder.Companion.aUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingPeopleStatesTest {

    private val anna = Friend(aUser().withId("annaId").build(), isFollowee = true)
    private val tom = Friend(aUser().withId("tomId").build(), isFollowee = true)
    private val peopleCatalog = InMemoryPeopleCatalog(
        mapOf(
            "jovId" to listOf(tom, anna)
        )
    )
    private val viewModel = PeopleViewModel(
        PeopleRepository(peopleCatalog),
        TestDispatchers()
    )

    @Test
    fun peopleStatesDeliveredToAnObserverInParticularOrder() {
        val deliveredStates = mutableListOf<PeopleState>()
        viewModel.peopleState.observeForever { deliveredStates.add(it) }

        viewModel.loadPeople("jovId")

        assertEquals(
            listOf(PeopleState.Loading, PeopleState.Loaded(listOf(tom, anna))),
            deliveredStates
        )
    }
}