package i.herman.timeline.state

import i.herman.domain.post.Post

sealed class TimelineState {

    data class Posts(val posts: List<Post>) : TimelineState()
}