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

    suspend fun updateFollowing(userId: String, followeeId: String): FollowState {
        return try {
            val toggleResult = userCatalog.toggleFollowing(userId, followeeId)
            if (toggleResult.isAdded) {
                FollowState.Followed(toggleResult.following)
            } else {
                FollowState.Unfollowed(toggleResult.following)
            }
        } catch (backendException: BackendException) {
            FollowState.BackendError
        }
    }
}