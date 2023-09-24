package i.herman.timeline

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.timeline.TimelineRepository
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.timeline.state.TimelineState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingTimelineStatesTest {

    private val timelineRepository = TimelineRepository(
        InMemoryUserCatalog(),
        InMemoryPostCatalog()
    )
    private val viewModel = TimelineViewModel(
        timelineRepository,
        TestDispatchers()
    )

    @Test
    fun timelineStatesDeliveredToAnObserverInParticularOrder() {
        val renderedStates = mutableListOf<TimelineState>()
        viewModel.timelineState.observeForever { renderedStates.add(it) }

        viewModel.timelineFor(":irrelevant:")

        assertEquals(
            listOf(TimelineState.Loading, TimelineState.Posts(emptyList())),
            renderedStates
        )
    }
}