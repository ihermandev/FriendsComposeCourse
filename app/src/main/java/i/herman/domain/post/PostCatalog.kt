package i.herman.domain.post

interface PostCatalog {

    fun postsFor(userIds: List<String>): List<Post>
}