package i.herman.friends

import androidx.lifecycle.SavedStateHandle
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.friends.FriendsRepository
import i.herman.domain.user.Following
import i.herman.domain.user.Friend
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.friends.state.FriendsScreenState
import i.herman.infrastructure.builder.UserBuilder.Companion.aUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class ToggleFollowingTest {

    private val dispatchers = TestDispatchers()
    private val savedStateHandle = SavedStateHandle()

    private val anna = aUser().withId("annaId").build()
    private val tom = aUser().withId("tomId").build()
    private val users = mutableMapOf(":irrelevant:" to mutableListOf(anna, tom))

    @Test
    fun follow() {
        val repository = FriendsRepository(InMemoryUserCatalog(users))
        val viewModel = FriendsViewModel(repository, dispatchers, savedStateHandle).apply {
            loadFriends(anna.id)
        }

        viewModel.toggleFollowing(anna.id, tom.id)

        assertEquals(
            FriendsScreenState(friends = listOf(Friend(tom, isFollower = true))),
            viewModel.screenState.value
        )
    }

    @Test
    fun unfollow() {
        val followings = mutableListOf(Following(tom.id, anna.id))
        val repository = FriendsRepository(InMemoryUserCatalog(users, followings))
        val viewModel = FriendsViewModel(repository, dispatchers, savedStateHandle).apply {
            loadFriends(tom.id)
        }

        viewModel.toggleFollowing(tom.id, anna.id)

        assertEquals(
            FriendsScreenState(friends = listOf(Friend(anna, isFollower = false))),
            viewModel.screenState.value
        )
    }
}