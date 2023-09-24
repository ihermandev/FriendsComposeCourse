package i.herman.postcomposer.state

import i.herman.domain.post.Post

sealed class CreatePostState {

    object Loading : CreatePostState()

    data class Created(val post: Post) : CreatePostState()

    object BackendError : CreatePostState()

    object Offline : CreatePostState()
}