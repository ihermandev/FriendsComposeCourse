package i.herman.timeline

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.post.Post
import i.herman.domain.post.PostCatalog
import i.herman.domain.timeline.TimelineRepository
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.timeline.state.TimelineState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailTimelineLoadingTest {

    @Test
    fun backendError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = UnavailablePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            TestDispatchers()
        )

        viewModel.timelineFor(":irrelevant:")

        assertEquals(TimelineState.BackendError, viewModel.timelineState.value)
    }

    @Test
    fun offlineError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = OfflinePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            TestDispatchers()
        )

        viewModel.timelineFor(":irrelevant:")

        assertEquals(TimelineState.OfflineError, viewModel.timelineState.value)
    }

    private class UnavailablePostCatalog : PostCatalog {
        override fun addPost(userId: String, postText: String): Post {
            TODO("Not yet implemented")
        }

        override suspend fun postsFor(userIds: List<String>): List<Post> {
            throw BackendException()
        }
    }

    private class OfflinePostCatalog : PostCatalog {
        override fun addPost(userId: String, postText: String): Post {
            TODO("Not yet implemented")
        }

        override suspend fun postsFor(userIds: List<String>): List<Post> {
            throw ConnectionUnavailableException()
        }
    }
}