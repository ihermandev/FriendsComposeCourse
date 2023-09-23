package i.herman.domain.user

data class Following(
    val userId: String,
    val followedId: String
)