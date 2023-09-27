package i.herman.timeline

import androidx.lifecycle.SavedStateHandle
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.post.Post
import i.herman.domain.timeline.TimelineRepository
import i.herman.domain.user.Following
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.infrastructure.builder.UserBuilder.Companion.aUser
import i.herman.timeline.state.TimelineScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class LoadTimelineTest {

    private val tim = aUser().withId("timId").build()
    private val anna = aUser().withId("annaId").build()
    private val lucy = aUser().withId("lucyId").build()
    private val sara = aUser().withId("saraId").build()

    private val timPosts = listOf(
        Post("postId", tim.id, "post text", 1L)
    )
    private val lucyPosts = listOf(
        Post("post2", lucy.id, "post 2", 2L),
        Post("post1", lucy.id, "post 1", 1L)
    )
    private val saraPosts = listOf(
        Post("post4", sara.id, "post 4", 4L),
        Post("post3", sara.id, "post 3", 3L)
    )

    private val availablePosts = (timPosts + lucyPosts + saraPosts).toMutableList()

    @Test
    fun noPostsAvailable() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = InMemoryPostCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.timelineFor("tomId")

        assertEquals(TimelineScreenState(posts = emptyList()), viewModel.screenState.value)
    }

    @Test
    fun postsAvailable() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = InMemoryPostCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.timelineFor(tim.id)

        assertEquals(TimelineScreenState(posts = timPosts), viewModel.screenState.value)
    }

    @Test
    fun postsFormFriends() {
        val userCatalog = InMemoryUserCatalog(followings = mutableListOf(Following(anna.id, lucy.id)))
        val postCatalog = InMemoryPostCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.timelineFor(anna.id)

        assertEquals(TimelineScreenState(posts = lucyPosts), viewModel.screenState.value)
    }

    @Test
    fun postsFromFriendsAlongOwn() {
        val userCatalog = InMemoryUserCatalog(followings = mutableListOf(Following(sara.id, lucy.id)))
        val postCatalog = InMemoryPostCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.timelineFor(sara.id)

        assertEquals(TimelineScreenState(posts = lucyPosts + saraPosts), viewModel.screenState.value)
    }
}