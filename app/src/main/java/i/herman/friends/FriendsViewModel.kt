package i.herman.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.R
import i.herman.app.CoroutineDispatchers
import i.herman.domain.friends.FriendsRepository
import i.herman.friends.state.FollowState
import i.herman.friends.state.FriendsScreenState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsViewModel(
    private val friendsRepository: FriendsRepository,
    private val dispatchers: CoroutineDispatchers,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val screenState: LiveData<FriendsScreenState> =
        savedStateHandle.getLiveData(SCREEN_STATE_KEY)

    fun loadFriends(userId: String) {
        viewModelScope.launch {
            updateScreenState(FriendsState.Loading)
            val result = withContext(dispatchers.background) {
                friendsRepository.loadFriendsFor(userId)
            }
            updateScreenState(result)
        }
    }

    fun toggleFollowing(userId: String, followerId: String) {
        when (val result = friendsRepository.updateFollowing(userId, followerId)) {
            is FollowState.Followed -> updateFollowingState(result.following.followedId, true)
            is FollowState.Unfollowed -> updateFollowingState(result.following.followedId, false)
        }
    }

    private fun updateFollowingState(followedId: String, isFollower: Boolean) {
        val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
        val index = currentState.friends.indexOfFirst { it.user.id == followedId }
        val matchingUser = currentState.friends[index]
        val updatedFriends = currentState.friends.toMutableList()
            .apply { set(index, matchingUser.copy(isFollower = isFollower)) }
        val updatedState = currentState.copy(friends = updatedFriends)
        savedStateHandle[SCREEN_STATE_KEY] = updatedState
    }

    private fun updateScreenState(friendsState: FriendsState) {
        val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
        val newState = when (friendsState) {
            is FriendsState.Loading -> currentState.copy(isLoading = true)
            is FriendsState.Loaded -> currentState.copy(isLoading = false, friends = friendsState.friends)
            is FriendsState.BackendError ->
                currentState.copy(isLoading = false, error = R.string.fetchingFriendsError)
            is FriendsState.Offline -> currentState.copy(isLoading = false, error = R.string.offlineError)
        }
        savedStateHandle[SCREEN_STATE_KEY] = newState
    }

    private companion object {
        private const val SCREEN_STATE_KEY = "friendsScreenState"
    }
}