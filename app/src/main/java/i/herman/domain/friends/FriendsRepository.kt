package i.herman.domain.friends

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.user.UserCatalog
import i.herman.friends.FriendsState
import i.herman.friends.state.FollowState



class FriendsRepository(
    private val userCatalog: UserCatalog
) {

    suspend fun loadFriendsFor(userId: String): FriendsState {
        return try {
            val friendsForUser = userCatalog.loadFriendsFor(userId)
            FriendsState.Loaded(friendsForUser)
        } catch (backendException: BackendException) {
            FriendsState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            FriendsState.Offline
        }
    }

    suspend fun updateFollowing(userId: String, followerId: String): FollowState {
        return try {
            toggleFollowing(userId, followerId)
        } catch (backendException: BackendException) {
            FollowState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            FollowState.Offline
        }
    }

    private suspend fun toggleFollowing(userId: String, followeeId: String): FollowState {
        val toggleResult = userCatalog.toggleFollowing(userId, followeeId)
        return if (toggleResult.isAdded) {
            FollowState.Followed(toggleResult.following)
        } else {
            FollowState.Unfollowed(toggleResult.following)
        }
    }
}