package i.herman.people

import i.herman.domain.user.Friend

sealed class PeopleState {

    object Loading : PeopleState()

    data class Loaded(val friends: List<Friend>) : PeopleState()

    object BackendError : PeopleState()

    object Offline : PeopleState()
}