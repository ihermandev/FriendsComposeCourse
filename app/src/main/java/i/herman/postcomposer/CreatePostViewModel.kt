package i.herman.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.domain.post.PostRepository
import i.herman.postcomposer.state.CreatePostState

class CreatePostViewModel(
    private val postRepository: PostRepository
) {

    private val mutablePostState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = mutablePostState

    fun createPost(postText: String) {
        val result = postRepository.createNewPost(postText)
        mutablePostState.value = result
    }
}