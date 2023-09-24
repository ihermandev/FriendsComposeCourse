package i.herman.postcomposer

import i.herman.InstantTaskExecutorExtension
import i.herman.domain.post.Post
import i.herman.domain.user.InMemoryUserData
import i.herman.infrastructure.ControllableClock
import i.herman.infrastructure.ControllableIdGenerator
import i.herman.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAPostTest {

    @Test
    fun aPostIsCreated() {
        val postText = "First Post"
        val post = Post("postId", "userId", postText, 1L)
        val viewModel = CreatePostViewModel(
            InMemoryUserData("userId"),
            ControllableClock(1L),
            ControllableIdGenerator("postId")
        )

        viewModel.createPost(postText)

        assertEquals(CreatePostState.Created(post), viewModel.postState.value)
    }

    @Test
    fun anotherPostCreated() {
        val postText = "Second Post"
        val anotherPost = Post("postId2", "userId", postText, 2L)
        val viewModel = CreatePostViewModel(
            InMemoryUserData("userId"),
            ControllableClock(2L),
            ControllableIdGenerator("postId2")
        )

        viewModel.createPost(postText)

        assertEquals(CreatePostState.Created(anotherPost), viewModel.postState.value)
    }
}