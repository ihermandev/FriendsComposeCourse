package i.herman.domain.post

interface PostCatalog {

    suspend fun postsFor(userIds: List<String>): List<Post>
}