package i.herman.timeline

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import org.junit.Rule
import org.junit.Test

class TimelineScreenTest {

    @get:Rule
    val timelineTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun showsEmptyTimelineMessage() {
        val email = "lucy@friends.com"
        val password = "passPASS123#@"
        launchTimelineFor(email, password, timelineTestRule) {
            //no operation
        } verify {
            emptyTimelineMessageIsDisplayed()
        }
    }
}