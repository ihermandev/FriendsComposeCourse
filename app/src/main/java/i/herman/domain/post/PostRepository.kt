package i.herman.domain.post

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.user.InMemoryUserData
import i.herman.postcomposer.state.CreatePostState


class PostRepository(
    private val userData: InMemoryUserData,
    private val postCatalog: PostCatalog,
) {

    suspend fun createNewPost(postText: String): CreatePostState {
        return try {
            val post = postCatalog.addPost(userData.loggedInUserId(), postText)
            CreatePostState.Created(post)
        } catch (backendException: BackendException) {
            CreatePostState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            CreatePostState.Offline
        }
    }
}