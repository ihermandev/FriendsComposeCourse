package i.herman.domain.post

import i.herman.domain.exceptions.ConnectionUnavailableException

class OfflinePostCatalog : PostCatalog {

    override fun addPost(userId: String, postText: String): Post {
        throw ConnectionUnavailableException()
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        throw ConnectionUnavailableException()
    }
}