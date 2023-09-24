package i.herman.postcomposer.state

import i.herman.domain.post.Post

sealed class CreatePostState {

    data class Created(val post: Post) : CreatePostState()
}