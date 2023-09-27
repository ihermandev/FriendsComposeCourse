package i.herman.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.domain.people.PeopleRepository

class PeopleViewModel(
    private val peopleRepository: PeopleRepository
) {

    private val mutablePeopleState = MutableLiveData<PeopleState>()
    val peopleState: LiveData<PeopleState> = mutablePeopleState

    fun loadPeople(userId: String) {
        val result = peopleRepository.loadPeopleFor(userId)
        mutablePeopleState.value = result
    }
}