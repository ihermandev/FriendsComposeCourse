package i.herman.postcomposer

import androidx.lifecycle.SavedStateHandle
import com.ihermandev.sharedtest.infrastructure.ControllableClock
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.post.Post
import i.herman.domain.post.PostRepository
import i.herman.domain.user.InMemoryUserDataStore
import i.herman.infrastructure.ControllableIdGenerator
import i.herman.postcomposer.state.CreateNewPostScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingCreatePostStatesTest {

    private val loggedInUserId = "userId"
    private val postId = "postId"
    private val timestamp = 1L
    private val text = "Post Text"
    private val post = Post(postId, loggedInUserId, text, timestamp)

    private val idGenerator = ControllableIdGenerator(postId)
    private val clock = ControllableClock(timestamp)
    private val postCatalog = InMemoryPostCatalog(idGenerator = idGenerator, clock = clock)
    private val userData = InMemoryUserDataStore(loggedInUserId)
    private val postRepository = PostRepository(userData, postCatalog)
    private val savedStateHandle = SavedStateHandle()
    private val dispatchers = TestDispatchers()
    private val viewModel = CreatePostViewModel(postRepository, savedStateHandle, dispatchers)

    @Test
    fun uiStatesAreDeliveredInParticularOrder() {
        val deliveredStates = mutableListOf<CreateNewPostScreenState>()
        viewModel.screenState.observeForever { deliveredStates.add(it) }

        viewModel.createPost(text)

        assertEquals(
            listOf(
                CreateNewPostScreenState(isLoading = true),
                CreateNewPostScreenState(createdPostId = post.id)
            ),
            deliveredStates
        )
    }
}