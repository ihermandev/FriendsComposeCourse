package i.herman.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.domain.user.Friend
import i.herman.domain.user.User

class PeopleViewModel {

    private val mutablePeopleState = MutableLiveData<PeopleState>()
    val peopleState: LiveData<PeopleState> = mutablePeopleState

    fun loadPeople(userId: String) {
        if (userId == "annaId") {
            val tom = Friend(User("tomId", "", ""), isFollowee = false)
            mutablePeopleState.value = PeopleState.Loaded(listOf(tom))
        } else if (userId == "lucyId") {
            val friendAnna = Friend(User("annaId", "", ""), true)
            val friendSara = Friend(User("saraId", "", ""), false)
            val friendTom = Friend(User("tomId", "", ""), false)
            mutablePeopleState.value = PeopleState.Loaded(listOf(friendAnna, friendSara, friendTom))
        } else {
            mutablePeopleState.value = PeopleState.Loaded(emptyList())
        }
    }
}