package i.herman.domain.post

class InMemoryPostCatalog(
    private val availablePosts: List<Post> = emptyList()
) : PostCatalog {

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        return availablePosts.filter { userIds.contains(it.userId) }
    }
}