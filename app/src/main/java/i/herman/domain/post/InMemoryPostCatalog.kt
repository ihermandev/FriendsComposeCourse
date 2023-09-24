package i.herman.domain.post

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.infrastructure.Clock
import i.herman.infrastructure.IdGenerator
import i.herman.infrastructure.SystemClock
import i.herman.infrastructure.UUIDGenerator

class InMemoryPostCatalog(
    private val availablePosts: List<Post> = emptyList(),
    private val idGenerator: IdGenerator = UUIDGenerator(),
    private val clock: Clock = SystemClock()
) : PostCatalog {

    override fun addPost(userId: String, postText: String): Post {
        if (postText == ":backend:") {
            throw BackendException()
        } else if (postText == ":offline:") {
            throw ConnectionUnavailableException()
        }
        val timestamp = clock.now()
        val postId = idGenerator.next()
        return Post(postId, userId, postText, timestamp)
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        return availablePosts.filter { userIds.contains(it.userId) }
    }
}