package i.herman.friends

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.friends.FriendsRepository
import i.herman.domain.user.Following
import i.herman.domain.user.Friend
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.infrastructure.builder.UserBuilder.Companion.aUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadFriendsTest {

    private val tom = aUser().withId("tomId").build()
    private val anna = aUser().withId("annaId").build()
    private val sara = aUser().withId("saraId").build()
    private val lucy = aUser().withId("lucyId").build()
    private val friendTom = Friend(tom, isFollower = false)
    private val friendAnna = Friend(anna, isFollower = true)
    private val friendSara = Friend(sara, isFollower = false)

    @Test
    fun noFriendsExisting() {
        val userCatalog = InMemoryUserCatalog()
        val viewModel = FriendsViewModel(FriendsRepository(userCatalog), TestDispatchers())

        viewModel.loadFriends(sara.id)

        assertEquals(FriendsState.Loaded(emptyList()), viewModel.friendsState.value)
    }

    @Test
    fun loadedASinglePerson() {
        val userCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(":irrelevant" to mutableListOf(tom))
        )
        val viewModel = FriendsViewModel(FriendsRepository(userCatalog), TestDispatchers())

        viewModel.loadFriends(anna.id)

        assertEquals(FriendsState.Loaded(listOf(friendTom)), viewModel.friendsState.value)
    }

    @Test
    fun loadedMultipleFriends() {
        val userCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(":irrelevant:" to mutableListOf(anna, sara, tom)),
            followings = mutableListOf(Following(lucy.id, anna.id))
        )
        val viewModel = FriendsViewModel(FriendsRepository(userCatalog), TestDispatchers())

        viewModel.loadFriends(lucy.id)

        assertEquals(
            FriendsState.Loaded(listOf(friendAnna, friendSara, friendTom)),
            viewModel.friendsState.value
        )
    }


    @Test
    fun loadedNoFriendsWhenUsingTheSignedUpUserId() {
        val userCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(":irrelevant:" to mutableListOf(tom))
        )
        val viewModel = FriendsViewModel(FriendsRepository(userCatalog), TestDispatchers())

        viewModel.loadFriends(tom.id)

        assertEquals(
            FriendsState.Loaded(emptyList()),
            viewModel.friendsState.value
        )
    }
}