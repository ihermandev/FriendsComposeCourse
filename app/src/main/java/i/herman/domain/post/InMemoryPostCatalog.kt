package i.herman.domain.post

import i.herman.infrastructure.Clock
import i.herman.infrastructure.IdGenerator
import i.herman.infrastructure.SystemClock
import i.herman.infrastructure.UUIDGenerator

class InMemoryPostCatalog(
    private val availablePosts: MutableList<Post> = mutableListOf(),
    private val idGenerator: IdGenerator = UUIDGenerator(),
    private val clock: Clock = SystemClock()
) : PostCatalog {

    override suspend fun addPost(userId: String, postText: String): Post {
        val timestamp = clock.now()
        val postId = idGenerator.next()
        val post = Post(postId, userId, postText, timestamp)
        availablePosts.add(post)
        return post
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        return availablePosts.filter { userIds.contains(it.userId) }
    }
}