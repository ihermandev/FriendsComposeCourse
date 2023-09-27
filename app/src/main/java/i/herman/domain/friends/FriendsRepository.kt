package i.herman.domain.friends

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.user.UserCatalog
import i.herman.friends.FriendsState


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
}