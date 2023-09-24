package i.herman.postcomposer

import i.herman.InstantTaskExecutorExtension
import i.herman.domain.post.Post
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
        val viewModel = CreatePostViewModel()

        viewModel.createPost(postText)

        assertEquals(CreatePostState.Created(post), viewModel.postState.value)
    }
}