package i.herman.friends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import i.herman.R
import i.herman.domain.user.Friend
import i.herman.ui.composables.ScreenTitle
import org.koin.androidx.compose.getViewModel

@Composable
fun FriendsScreen(
    userId: String
) {

    val friendsViewModel = getViewModel<FriendsViewModel>()
    if (friendsViewModel.friendsState.value == null) {
        friendsViewModel.loadFriends(userId)
    }

    val friendsState = friendsViewModel.friendsState.observeAsState().value

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(resource = R.string.friends)
            Spacer(modifier = Modifier.height(16.dp))
            if (friendsState is FriendsState.Loaded) {
                FriendsList(friendsState.friends)
            }
        }
    }
}

@Composable
private fun FriendsList(
    friends: List<Friend>,
    modifier: Modifier = Modifier
) {
    if (friends.isEmpty()) {
        Text(text = stringResource(id = R.string.emptyFriendsMessage))
    } else {
        LazyColumn {
            items(friends) { friend ->
                Text(text = friend.user.id)
            }
        }
    }
}