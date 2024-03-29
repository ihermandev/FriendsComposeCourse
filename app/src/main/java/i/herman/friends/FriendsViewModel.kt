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
        viewModelScope.launch {
            updateListOfTogglingFriendships(followerId)
            val updateFollowing = withContext(dispatchers.background) {
                friendsRepository.updateFollowing(userId, followerId)
            }
            when (updateFollowing) {
                is FollowState.Followed -> setFollower(updateFollowing.following.followedId)
                is FollowState.Unfollowed -> removeFollower(updateFollowing.following.followedId)
                is FollowState.BackendError -> errorUpdatingFollowing(followerId, R.string.errorFollowingFriend)
                is FollowState.Offline -> errorUpdatingFollowing(followerId, R.string.offlineError)
            }
        }
    }

    private fun errorUpdatingFollowing(followerId: String, errorResource: Int) {
        val currentState = currentScreenState()
        val newState = currentState.copy(
            error = errorResource,
            updatingFriends = currentState.updatingFriends - listOf(followerId)
        )
        updateScreenState(newState)
    }

    private fun updateListOfTogglingFriendships(followerId: String) {
        val currentState = currentScreenState()
        val updatedList = currentState.updatingFriends + listOf(followerId)
        updateScreenState(currentState.copy(updatingFriends = updatedList))
    }

    private fun setFollower(followerId: String) {
        updateFollowingState(followerId, true)
    }

    private fun removeFollower(followerId: String) {
        updateFollowingState(followerId, false)
    }

    private fun updateFollowingState(followedId: String, isFollower: Boolean) {
        val currentState = currentScreenState()
        val index = currentState.friends.indexOfFirst { it.user.id == followedId }
        val matchingUser = currentState.friends[index]
        val updatedFriends = currentState.friends.toMutableList()
            .apply { set(index, matchingUser.copy(isFollower = isFollower)) }
        val updatedToggles = currentState.updatingFriends - listOf(followedId)
        val updatedState = currentState.copy(friends = updatedFriends, updatingFriends = updatedToggles)
        updateScreenState(updatedState)
    }

    private fun updateScreenState(friendsState: FriendsState) {
        val currentState = currentScreenState()
        val newState = when (friendsState) {
            is FriendsState.Loading -> currentState.copy(isLoading = true)
            is FriendsState.Loaded -> currentState.copy(isLoading = false, friends = friendsState.friends)
            is FriendsState.BackendError ->
                currentState.copy(isLoading = false, error = R.string.fetchingFriendsError)
            is FriendsState.Offline -> currentState.copy(isLoading = false, error = R.string.offlineError)
        }
        updateScreenState(newState)
    }

    private fun currentScreenState(): FriendsScreenState {
        return savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
    }

    private fun updateScreenState(newState: FriendsScreenState) {
        savedStateHandle[SCREEN_STATE_KEY] = newState
    }

    private companion object {
        private const val SCREEN_STATE_KEY = "friendsScreenState"
    }
}