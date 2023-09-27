package i.herman.friends

import androidx.lifecycle.SavedStateHandle
import com.ihermandev.sharedtest.domain.user.OfflineUserCatalog
import com.ihermandev.sharedtest.domain.user.UnavailableUserCatalog
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.friends.FriendsRepository
import i.herman.friends.state.FriendsScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import i.herman.R

@ExtendWith(InstantTaskExecutorExtension::class)
class FailUpdatingFollowingTest {

    private val dispatchers = TestDispatchers()
    private val savedStateHandle = SavedStateHandle()

    @Test
    fun backendError() {
        val friendsRepository = FriendsRepository(UnavailableUserCatalog())
        val viewModel = FriendsViewModel(friendsRepository, dispatchers, savedStateHandle)

        viewModel.toggleFollowing(":userId:", ":followerId:")

        assertEquals(
            FriendsScreenState(error = R.string.errorFollowingFriend),
            viewModel.screenState.value
        )
    }

    @Test
    fun offlineError() {
        val friendsRepository = FriendsRepository(OfflineUserCatalog())
        val viewModel = FriendsViewModel(friendsRepository, dispatchers, savedStateHandle)

        viewModel.toggleFollowing(":userId:", ":followerId:")

        assertEquals(
            FriendsScreenState(error = R.string.offlineError),
            viewModel.screenState.value
        )
    }
}