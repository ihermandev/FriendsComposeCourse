package i.herman.postcomposer

import androidx.lifecycle.SavedStateHandle
import com.ihermandev.sharedtest.domain.post.OfflinePostCatalog
import com.ihermandev.sharedtest.domain.post.UnavailablePostCatalog
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.post.PostRepository
import i.herman.domain.user.InMemoryUserDataStore
import i.herman.postcomposer.state.CreateNewPostScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import i.herman.R


@ExtendWith(InstantTaskExecutorExtension::class)
class FailedPostCreationTest {

    @Test
    fun backedError() {
        val viewModel = CreatePostViewModel(
            PostRepository(
                InMemoryUserDataStore("userId"),
                UnavailablePostCatalog()
            ),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.createPost(":backend:")

        assertEquals(
            CreateNewPostScreenState(error = R.string.creatingPostError),
            viewModel.screenState.value
        )
    }

    @Test
    fun offlineError() {
        val viewModel = CreatePostViewModel(
            PostRepository(
                InMemoryUserDataStore("userId"),
                OfflinePostCatalog()
            ),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.createPost(":offline:")

        assertEquals(
            CreateNewPostScreenState(error = R.string.offlineError),
            viewModel.screenState.value
        )
    }
}