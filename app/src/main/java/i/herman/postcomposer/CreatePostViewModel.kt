package i.herman.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.domain.post.PostRepository
import i.herman.postcomposer.state.CreatePostState
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val mutablePostState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = mutablePostState

    fun createPost(postText: String) {
        viewModelScope.launch {
            val result = postRepository.createNewPost(postText)
            mutablePostState.value = result
        }
    }
}