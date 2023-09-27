package i.herman.domain.friends

import i.herman.domain.user.Following

data class ToggleFollowing(
    val following: Following,
    val isAdded: Boolean
)