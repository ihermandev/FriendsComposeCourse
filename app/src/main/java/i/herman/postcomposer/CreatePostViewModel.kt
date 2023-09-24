package i.herman.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.app.CoroutineDispatchers
import i.herman.domain.post.PostRepository
import i.herman.postcomposer.state.CreatePostState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePostViewModel(
    private val postRepository: PostRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val mutablePostState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = mutablePostState

    fun createPost(postText: String) {
        viewModelScope.launch {
            mutablePostState.value = CreatePostState.Loading
            mutablePostState.value = withContext(dispatchers.background) {
                postRepository.createNewPost(postText)
            }
        }
    }
}