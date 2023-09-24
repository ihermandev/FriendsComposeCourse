package i.herman.domain.post

interface PostCatalog {

    fun addPost(userId: String, postText: String): Post

    suspend fun postsFor(userIds: List<String>): List<Post>
}