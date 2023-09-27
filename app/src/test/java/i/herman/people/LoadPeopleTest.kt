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
        val user = User("tomId", "", "")
        val tomFriend = Friend(user, isFollowee = false)
        val viewModel = PeopleViewModel()

        viewModel.loadPeople("annaId")

        assertEquals(PeopleState.Loaded(listOf(tomFriend)), viewModel.peopleState.value)
    }

    @Test
    fun loadedMultiplePeople() {
        val friendAnna = Friend(User("annaId", "", ""), true)
        val friendSara = Friend(User("saraId", "", ""), false)
        val friendTom = Friend(User("tomId", "", ""), false)
        val viewModel = PeopleViewModel()

        viewModel.loadPeople("lucyId")

        assertEquals(
            PeopleState.Loaded(listOf(friendAnna, friendSara, friendTom)),
            viewModel.peopleState.value
        )
    }
}