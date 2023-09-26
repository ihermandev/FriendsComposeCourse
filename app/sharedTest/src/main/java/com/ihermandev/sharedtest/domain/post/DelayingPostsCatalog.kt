package com.ihermandev.sharedtest.domain.post

import i.herman.domain.post.Post
import i.herman.domain.post.PostCatalog
import kotlinx.coroutines.delay

class DelayingPostsCatalog : PostCatalog {

    override suspend fun addPost(userId: String, postText: String): Post {
        delay(2000)
        return Post("postId", userId, postText, 0)
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        delay(2000)
        return emptyList()
    }
}