package i.herman.postcomposer

import i.herman.InstantTaskExecutorExtension
import i.herman.domain.post.PostRepository
import i.herman.domain.user.InMemoryUserData
import i.herman.infrastructure.ControllableClock
import i.herman.infrastructure.ControllableIdGenerator
import i.herman.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class FailedPostCreationTest {

    @Test
    fun backedError() {
        val userData = InMemoryUserData("userId")
        val clock = ControllableClock(1L)
        val idGenerator = ControllableIdGenerator("postId1")
        val viewModel = CreatePostViewModel(
            PostRepository(userData, clock, idGenerator)
        )

        viewModel.createPost(":backend:")

        assertEquals(CreatePostState.BackendError, viewModel.postState.value)
    }

    @Test
    fun offlineError() {
        val userData = InMemoryUserData("userId")
        val clock = ControllableClock(1L)
        val idGenerator = ControllableIdGenerator("postId2")
        val viewModel = CreatePostViewModel(
            PostRepository(userData, clock, idGenerator)
        )

        viewModel.createPost(":offline:")

        assertEquals(CreatePostState.Offline, viewModel.postState.value)
    }
}