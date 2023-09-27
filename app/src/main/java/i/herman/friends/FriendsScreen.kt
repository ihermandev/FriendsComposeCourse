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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
                FriendsList(
                    friends = friendsState.friends
                )
            }
        }
    }
}

@Composable
private fun FriendsList(
    friends: List<Friend>
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
                FriendItem(friend)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FriendItem(
    friend: Friend,
    modifier: Modifier = Modifier
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
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.follow))
            }
        }
    }
}