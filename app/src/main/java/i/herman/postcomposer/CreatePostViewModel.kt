package i.herman.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.domain.post.Post
import i.herman.domain.user.InMemoryUserData
import i.herman.infrastructure.Clock
import i.herman.infrastructure.IdGenerator
import i.herman.postcomposer.state.CreatePostState

class CreatePostViewModel(
    private val userData: InMemoryUserData,
    private val clock: Clock,
    private val idGenerator: IdGenerator
) {

    private val mutablePostState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = mutablePostState

    fun createPost(postText: String) {
        val post = createNewPost(postText)
        mutablePostState.value = CreatePostState.Created(post)
    }

    private fun createNewPost(postText: String): Post {
        val userId = userData.loggedInUserId()
        val timestamp = clock.now()
        val postId = idGenerator.next()
        return Post(postId, userId, postText, timestamp)
    }
}