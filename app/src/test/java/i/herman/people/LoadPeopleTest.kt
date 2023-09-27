package i.herman.people

import i.herman.InstantTaskExecutorExtension
import i.herman.domain.user.Friend
import i.herman.domain.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPeopleTest {

    @Test
    fun noPeopleExisting() {
        val viewModel = PeopleViewModel()

        viewModel.loadPeople("saraId")

        assertEquals(PeopleState.Loaded(emptyList()), viewModel.peopleState.value)
    }

    @Test
    fun loadedASinglePerson() {
        val tom = Friend(User("tomId", "", ""), isFollowee = false)
        val viewModel = PeopleViewModel()

        viewModel.loadPeople("annaId")

        assertEquals(PeopleState.Loaded(listOf(tom)), viewModel.peopleState.value)
    }

    @Test
    fun loadedMultiplePeople() {
        val anna = Friend(User("annaId", "", ""), isFollowee = true)
        val sara = Friend(User("saraId", "", ""), isFollowee = false)
        val tom = Friend(User("tomId", "", ""), isFollowee = false)
        val viewModel = PeopleViewModel()

        viewModel.loadPeople("lucyId")

        assertEquals(
            PeopleState.Loaded(listOf(anna, sara, tom)),
            viewModel.peopleState.value
        )
    }
}