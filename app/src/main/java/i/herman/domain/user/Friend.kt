package i.herman.domain.user

data class Friend(
    val user: User,
    val isFollower: Boolean
)