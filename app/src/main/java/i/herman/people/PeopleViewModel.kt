package i.herman.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.app.CoroutineDispatchers
import i.herman.domain.people.PeopleRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeopleViewModel(
    private val peopleRepository: PeopleRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val mutablePeopleState = MutableLiveData<PeopleState>()
    val peopleState: LiveData<PeopleState> = mutablePeopleState

    fun loadPeople(userId: String) {
        viewModelScope.launch {
            mutablePeopleState.value = PeopleState.Loading
            mutablePeopleState.value = withContext(dispatchers.background) {
                peopleRepository.loadPeopleFor(userId)
            }
        }
    }
}