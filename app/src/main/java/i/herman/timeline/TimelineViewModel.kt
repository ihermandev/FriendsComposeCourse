package i.herman.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.domain.timeline.TimelineRepository
import i.herman.timeline.state.TimelineState


class TimelineViewModel(
    private val timelineRepository: TimelineRepository
) {

    private val mutableTimelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = mutableTimelineState

    fun timelineFor(userId: String) {
        val result = timelineRepository.getTimelineFor(userId)
        mutableTimelineState.value = result
    }
}