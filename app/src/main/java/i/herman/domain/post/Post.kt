package i.herman.domain.post

data class Post(
    val id: String,
    val userId: String,
    val text: String,
    val timestamp: Long
)