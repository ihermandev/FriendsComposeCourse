package i.herman.timeline.state

import android.os.Parcelable
import androidx.annotation.StringRes
import i.herman.domain.post.Post
import kotlinx.parcelize.Parcelize


@Parcelize
data class TimelineScreenState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    @StringRes val error: Int = 0
) : Parcelable