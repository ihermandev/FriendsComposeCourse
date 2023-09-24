package i.herman.postcomposer

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.post.Post
import i.herman.domain.post.PostRepository
import i.herman.domain.user.InMemoryUserData
import i.herman.infrastructure.ControllableClock
import i.herman.infrastructure.ControllableIdGenerator
import i.herman.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingCreatePostStatesTest {

    private val loggedInUserId = "userId"
    private val postId = "postId"
    private val timestamp = 1L
    private val text = "Post Text"

    private val idGenerator = ControllableIdGenerator(postId)
    private val clock = ControllableClock(timestamp)
    private val postCatalog = InMemoryPostCatalog(idGenerator = idGenerator, clock = clock)
    private val userData = InMemoryUserData(loggedInUserId)
    private val postRepository = PostRepository(userData, postCatalog)
    private val dispatchers = TestDispatchers()
    private val viewModel = CreatePostViewModel(postRepository, dispatchers)

    @Test
    fun uiStatesAreDeliveredInParticularOrder() {
        val deliveredStates = mutableListOf<CreatePostState>()
        viewModel.postState.observeForever { deliveredStates.add(it) }
        val post = Post(postId, loggedInUserId, text, timestamp)

        viewModel.createPost(text)

        assertEquals(
            listOf(CreatePostState.Loading, CreatePostState.Created(post)),
            deliveredStates
        )
    }
}