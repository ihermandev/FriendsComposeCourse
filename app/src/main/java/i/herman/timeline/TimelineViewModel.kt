package i.herman.timeline

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.R
import i.herman.app.CoroutineDispatchers
import i.herman.domain.post.Post
import i.herman.domain.timeline.TimelineRepository
import i.herman.timeline.state.TimelineScreenState
import i.herman.timeline.state.TimelineState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TimelineViewModel(
    private val timelineRepository: TimelineRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val screenState: LiveData<TimelineScreenState> =
        savedStateHandle.getLiveData(SCREEN_STATE_KEY)

    fun timelineFor(userId: String) {
        viewModelScope.launch {
            updateScreenStateFor(TimelineState.Loading)
            val result = withContext(dispatchers.background) {
                timelineRepository.getTimelineFor(userId)
            }
            updateScreenStateFor(result)
        }
    }

    private fun updateScreenStateFor(timelineState: TimelineState) {
        when (timelineState) {
            is TimelineState.Loading -> setLoading()
            is TimelineState.Posts -> setPosts(timelineState.posts)
            is TimelineState.BackendError -> setError(R.string.fetchingTimelineError)
            is TimelineState.OfflineError -> setError(R.string.offlineError)
        }
    }

    private fun setLoading() {
        val screenState = currentScreenState()
        updateScreenState(screenState.copy(isLoading = true))
    }

    private fun setPosts(posts: List<Post>) {
        val screenState = currentScreenState()
        updateScreenState(screenState.copy(isLoading = false, posts = posts))
    }

    private fun setError(@StringRes errorResource: Int) {
        val screenState = currentScreenState()
        updateScreenState(screenState.copy(isLoading = false, error = errorResource))
    }

    private fun currentScreenState(): TimelineScreenState {
        return savedStateHandle[SCREEN_STATE_KEY] ?: TimelineScreenState()
    }

    private fun updateScreenState(newState: TimelineScreenState) {
        savedStateHandle[SCREEN_STATE_KEY] = newState
    }

    private companion object {
        private const val SCREEN_STATE_KEY = "timelineScreenState"
    }
}