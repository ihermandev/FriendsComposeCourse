package i.herman.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    val user: User,
    val isFollower: Boolean,
) : Parcelable