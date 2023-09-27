package i.herman.friends.state

import i.herman.domain.user.Following

sealed class FollowState {

    data class Followed(val following: Following) : FollowState()

    data class Unfollowed(val following: Following) : FollowState()

    object BackendError : FollowState()

    object Offline : FollowState()
}