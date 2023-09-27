package i.herman.friends.state

import android.os.Parcelable
import androidx.annotation.StringRes
import i.herman.domain.user.Friend
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendsScreenState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    @StringRes val error: Int = 0
) : Parcelable