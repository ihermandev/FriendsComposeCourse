package i.herman.timeline

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.post.Post
import i.herman.domain.timeline.TimelineRepository
import i.herman.domain.user.Following
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.infrastructure.builder.UserBuilder.Companion.aUser
import i.herman.timeline.state.TimelineState
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
            TestDispatchers()
        )

        viewModel.timelineFor("tomId")

        assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
    }

    @Test
    fun postsAvailable() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = InMemoryPostCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            TestDispatchers()
        )

        viewModel.timelineFor(tim.id)

        assertEquals(TimelineState.Posts(timPosts), viewModel.timelineState.value)
    }

    @Test
    fun postsFormFriends() {
        val userCatalog = InMemoryUserCatalog(
            followings = listOf(
                Following(anna.id, lucy.id)
            )
        )
        val postCatalog = InMemoryPostCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            TestDispatchers()
        )

        viewModel.timelineFor(anna.id)

        assertEquals(TimelineState.Posts(lucyPosts), viewModel.timelineState.value)
    }

    @Test
    fun postsFromFriendsAlongOwn() {
        val userCatalog = InMemoryUserCatalog(
            followings = listOf(
                Following(sara.id, lucy.id)
            )
        )
        val postCatalog = InMemoryPostCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            TestDispatchers()
        )

        viewModel.timelineFor(sara.id)

        assertEquals(TimelineState.Posts(lucyPosts + saraPosts), viewModel.timelineState.value)
    }
}