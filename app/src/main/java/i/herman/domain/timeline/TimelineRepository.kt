package i.herman.domain.timeline

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.post.PostCatalog
import i.herman.domain.user.UserCatalog
import i.herman.timeline.state.TimelineState

class TimelineRepository(
    private val userCatalog: UserCatalog,
    private val postCatalog: PostCatalog
) {

    fun getTimelineFor(userId: String): TimelineState {
        return try {
            val userIds = listOf(userId) + userCatalog.followedBy(userId)
            val postsForUser = postCatalog.postsFor(userIds)
            TimelineState.Posts(postsForUser)
        } catch (backendException: BackendException) {
            TimelineState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            TimelineState.OfflineError
        }
    }
}