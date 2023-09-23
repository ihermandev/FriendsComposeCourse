package i.herman.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.user.UserCatalog
import i.herman.timeline.state.TimelineState

class TimelineViewModel(
    private val userCatalog: UserCatalog,
    private val postCatalog: InMemoryPostCatalog
) {

    private val mutableTimelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = mutableTimelineState

    fun timelineFor(userId: String) {
        val userIds = listOf(userId) + userCatalog.followedBy(userId)
        val postsForUser = postCatalog.postsFor(userIds)
        mutableTimelineState.value = TimelineState.Posts(postsForUser)
    }
}