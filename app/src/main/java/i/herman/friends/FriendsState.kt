package i.herman.friends

import i.herman.domain.user.Friend

sealed class FriendsState {

    object Loading : FriendsState()

    data class Loaded(val friends: List<Friend>) : FriendsState()

    object BackendError : FriendsState()

    object Offline : FriendsState()
}