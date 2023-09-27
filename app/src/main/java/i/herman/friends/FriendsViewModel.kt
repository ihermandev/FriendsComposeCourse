package i.herman.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.app.CoroutineDispatchers
import i.herman.domain.friends.FriendsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsViewModel(
    private val friendsRepository: FriendsRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val mutableFriendsState = MutableLiveData<FriendsState>()
    val friendsState: LiveData<FriendsState> = mutableFriendsState

    fun loadFriends(userId: String) {
        viewModelScope.launch {
            mutableFriendsState.value = FriendsState.Loading
            mutableFriendsState.value = withContext(dispatchers.background) {
                friendsRepository.loadFriendsFor(userId)
            }
        }
    }
}