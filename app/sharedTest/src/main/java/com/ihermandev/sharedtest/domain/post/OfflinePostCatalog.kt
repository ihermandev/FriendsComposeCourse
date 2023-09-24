package com.ihermandev.sharedtest.domain.post

import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.post.Post
import i.herman.domain.post.PostCatalog

class OfflinePostCatalog : PostCatalog {

    override fun addPost(userId: String, postText: String): Post {
        throw ConnectionUnavailableException()
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        throw ConnectionUnavailableException()
    }
}