package i.herman.friends

import com.ihermandev.sharedtest.domain.user.OfflineUserCatalog
import com.ihermandev.sharedtest.domain.user.UnavailableUserCatalog
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.friends.FriendsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith



@ExtendWith(InstantTaskExecutorExtension::class)
class FailFriendsLoadingTest {

    @Test
    fun backendError() {
        val viewModel = FriendsViewModel(
            FriendsRepository(
                UnavailableUserCatalog()
            ), TestDispatchers()
        )

        viewModel.loadFriends(":irrelevant:")

        assertEquals(FriendsState.BackendError, viewModel.friendsState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = FriendsViewModel(
            FriendsRepository(
                OfflineUserCatalog()
            ),
            TestDispatchers()
        )

        viewModel.loadFriends(":irrelevant:")

        assertEquals(FriendsState.Offline, viewModel.friendsState.value)
    }
}