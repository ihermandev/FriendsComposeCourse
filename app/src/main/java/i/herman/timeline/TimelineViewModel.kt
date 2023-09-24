package i.herman.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.app.CoroutineDispatchers
import i.herman.domain.timeline.TimelineRepository
import i.herman.timeline.state.TimelineState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TimelineViewModel(
    private val timelineRepository: TimelineRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val mutableTimelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = mutableTimelineState

    fun timelineFor(userId: String) {
        viewModelScope.launch {
            mutableTimelineState.value = TimelineState.Loading
            mutableTimelineState.value = withContext(dispatchers.background) {
                timelineRepository.getTimelineFor(userId)
            }
        }
    }
}