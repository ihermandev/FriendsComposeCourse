package com.ihermandev.sharedtest.domain.post

import i.herman.domain.exceptions.BackendException
import i.herman.domain.post.Post
import i.herman.domain.post.PostCatalog

class UnavailablePostCatalog : PostCatalog {

    override fun addPost(userId: String, postText: String): Post {
        throw BackendException()
    }

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        throw BackendException()
    }
}