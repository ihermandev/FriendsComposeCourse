package i.herman.postcomposer

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
class CreateAPostTest {

    @Test
    fun aPostIsCreated() {
        val postText = "First Post"
        val post = Post("postId", "userId", postText, 1L)
        val userData = InMemoryUserDataStore("userId")
        val clock = ControllableClock(1L)
        val idGenerator = ControllableIdGenerator("postId")
        val viewModel = CreatePostViewModel(
            PostRepository(
                userData, InMemoryPostCatalog(
                    idGenerator = idGenerator,
                    clock = clock
                )
            ),
            TestDispatchers()
        )

        viewModel.createPost(postText)

        assertEquals(CreateNewPostScreenState(createdPostId = post.id), viewModel.postScreenState.value)
    }

    @Test
    fun anotherPostCreated() {
        val postText = "Second Post"
        val anotherPost = Post("postId2", "userId", postText, 2L)
        val userData = InMemoryUserDataStore("userId")
        val clock = ControllableClock(2L)
        val idGenerator = ControllableIdGenerator("postId2")
        val viewModel = CreatePostViewModel(
            PostRepository(
                userData, InMemoryPostCatalog(
                    idGenerator = idGenerator,
                    clock = clock
                )
            ),
            TestDispatchers()
        )

        viewModel.createPost(postText)

        assertEquals(
            CreateNewPostScreenState(createdPostId = anotherPost.id),
            viewModel.postScreenState.value
        )
    }
}