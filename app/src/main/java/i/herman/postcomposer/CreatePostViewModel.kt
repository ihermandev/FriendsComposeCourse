package i.herman.postcomposer

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.R
import i.herman.app.CoroutineDispatchers
import i.herman.domain.post.Post
import i.herman.domain.post.PostRepository
import i.herman.postcomposer.state.CreateNewPostScreenState
import i.herman.postcomposer.state.CreatePostState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePostViewModel(
    private val postRepository: PostRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val screenState: LiveData<CreateNewPostScreenState> =
        savedStateHandle.getLiveData(SCREEN_STATE_KEY)

    fun updatePostText(postText: String) {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(postText = postText))
    }

    fun createPost(postText: String) {
        viewModelScope.launch {
            setLoading()
            val result = withContext(dispatchers.background) {
                postRepository.createNewPost(postText)
            }
            updateScreenStateFor(result)
        }
    }

    private fun updateScreenStateFor(createPostState: CreatePostState) {
        when (createPostState) {
            is CreatePostState.Created -> setPostCreated(createPostState.post)
            is CreatePostState.BackendError -> setError(R.string.creatingPostError)
            is CreatePostState.Offline -> setError(R.string.offlineError)
        }
    }

    private fun setLoading() {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(isLoading = true))
    }

    private fun setPostCreated(post: Post) {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(isLoading = false, createdPostId = post.id))
    }

    private fun setError(@StringRes errorResource: Int) {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(isLoading = false, error = errorResource))
    }

    private fun currentScreenState(): CreateNewPostScreenState {
        return savedStateHandle[SCREEN_STATE_KEY] ?: CreateNewPostScreenState()
    }

    private fun updateScreenState(newScreenState: CreateNewPostScreenState) {
        savedStateHandle[SCREEN_STATE_KEY] = newScreenState
    }

    private companion object {
        private const val SCREEN_STATE_KEY = "createPostScreenState"
    }
}