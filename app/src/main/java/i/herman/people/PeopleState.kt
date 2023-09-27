package i.herman.people

import i.herman.domain.user.Friend

sealed class PeopleState {

    data class Loaded(val friends: List<Friend>): PeopleState()
}