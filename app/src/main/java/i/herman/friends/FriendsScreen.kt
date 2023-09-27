package i.herman.friends

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import i.herman.R
import i.herman.domain.user.Friend
import i.herman.friends.state.FriendsScreenState
import i.herman.ui.composables.InfoMessage
import i.herman.ui.composables.ScreenTitle
import org.koin.androidx.compose.getViewModel

@Composable
fun FriendsScreen(
    userId: String
) {
    val friendsViewModel = getViewModel<FriendsViewModel>()
    var loadedUserId by remember { mutableStateOf("") }
    val screenState = friendsViewModel.screenState.observeAsState().value ?: FriendsScreenState()

    if (loadedUserId != userId) {
        loadedUserId = userId
        friendsViewModel.loadFriends(loadedUserId)
    }
    FriendsScreenContent(
        screenState = screenState,
        onRefresh = { friendsViewModel.loadFriends(userId) },
        toggleFollowingFor = { friendsViewModel.toggleFollowing(userId, it) }
    )
}

@Composable
private fun FriendsScreenContent(
    modifier: Modifier = Modifier,
    screenState: FriendsScreenState,
    onRefresh: () -> Unit,
    toggleFollowingFor: (String) -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(resource = R.string.friends)
            Spacer(modifier = Modifier.height(16.dp))
            FriendsList(
                isRefreshing = screenState.isLoading,
                friends = screenState.friends,
                currentlyUpdatingFriends = screenState.updatingFriends,
                onRefresh = { onRefresh() },
                toggleFollowingFor = { toggleFollowingFor(it) }
            )
        }
        InfoMessage(stringResource = screenState.error)
    }
}

@Composable
private fun FriendsList(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    friends: List<Friend>,
    currentlyUpdatingFriends: List<String>,
    onRefresh: () -> Unit,
    toggleFollowingFor: (String) -> Unit
) {
    val loadingContentDescription = stringResource(id = R.string.loading)
    SwipeRefresh(
        modifier = modifier.semantics { contentDescription = loadingContentDescription },
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh() }
    ) {
        if (friends.isEmpty()) {
            Text(
                text = stringResource(id = R.string.emptyFriendsMessage),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(friends) { friend ->
                    FriendItem(
                        friend = friend,
                        isTogglingFriendship = friend.user.id in currentlyUpdatingFriends,
                        toggleFollowingFor = { toggleFollowingFor(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun FriendItem(
    modifier: Modifier = Modifier,
    friend: Friend,
    isTogglingFriendship: Boolean,
    toggleFollowingFor: (String) -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = friend.user.id)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = friend.user.about)
            }
            val followContentDescription = stringResource(R.string.followFriend, friend.user.id)
            val unfollowContentDescription = stringResource(R.string.unfollowFriend, friend.user.id)
            val updatingContentDescription = stringResource(R.string.updatingFriendship, friend.user.id)
            OutlinedButton(
                modifier = Modifier.semantics {
                    contentDescription =
                        if (friend.isFollower) unfollowContentDescription else followContentDescription
                },
                enabled = !isTogglingFriendship,
                onClick = { toggleFollowingFor(friend.user.id) }
            ) {
                if (isTogglingFriendship) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(16.dp)
                            .semantics { contentDescription = updatingContentDescription },
                        strokeWidth = 2.dp
                    )
                } else {
                    val resource = if (friend.isFollower) R.string.unfollow else R.string.follow
                    Text(text = stringResource(id = resource))
                }
            }
        }
    }
}