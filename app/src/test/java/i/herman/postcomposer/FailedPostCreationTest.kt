package i.herman.postcomposer

import com.ihermandev.sharedtest.domain.post.OfflinePostCatalog
import com.ihermandev.sharedtest.domain.post.UnavailablePostCatalog
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.post.PostRepository
import i.herman.domain.user.InMemoryUserData
import i.herman.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class FailedPostCreationTest {

    @Test
    fun backedError() {
        val viewModel = CreatePostViewModel(
            PostRepository(
                InMemoryUserData("userId"),
                UnavailablePostCatalog()
            ),
            TestDispatchers()
        )

        viewModel.createPost(":backend:")

        assertEquals(CreatePostState.BackendError, viewModel.postState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = CreatePostViewModel(
            PostRepository(
                InMemoryUserData("userId"),
                OfflinePostCatalog()
            ),
            TestDispatchers()
        )

        viewModel.createPost(":offline:")

        assertEquals(CreatePostState.Offline, viewModel.postState.value)
    }
}