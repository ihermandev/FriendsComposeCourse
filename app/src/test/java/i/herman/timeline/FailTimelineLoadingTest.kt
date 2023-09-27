package i.herman.timeline

import androidx.lifecycle.SavedStateHandle
import com.ihermandev.sharedtest.domain.post.OfflinePostCatalog
import com.ihermandev.sharedtest.domain.post.UnavailablePostCatalog
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.timeline.TimelineRepository
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.timeline.state.TimelineScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import i.herman.R

@ExtendWith(InstantTaskExecutorExtension::class)
class FailTimelineLoadingTest {

    @Test
    fun backendError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = UnavailablePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.timelineFor(":irrelevant:")

        assertEquals(
            TimelineScreenState(error = R.string.fetchingTimelineError),
            viewModel.screenState.value
        )
    }

    @Test
    fun offlineError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = OfflinePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers()
        )

        viewModel.timelineFor(":irrelevant:")

        assertEquals(
            TimelineScreenState(error = R.string.offlineError),
            viewModel.screenState.value
        )
    }
}