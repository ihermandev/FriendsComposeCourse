package i.herman.timeline

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import i.herman.MainActivity
import i.herman.signup.launchSignUpScreen
import i.herman.R
import i.herman.domain.post.Post

typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchTimelineFor(
    email: String,
    password: String,
    timelineTestRule: MainActivityRule,
    block: TimelineRobot.() -> Unit
): TimelineRobot {
    launchSignUpScreen(timelineTestRule) {
        typeEmail(email)
        typePassword(password)
        submit()
    }
    return TimelineRobot(timelineTestRule).apply(block)
}

class TimelineRobot(
    private val rule: MainActivityRule
) {

    infix fun verify(
        block: TimelineVerificationRobot.() -> Unit
    ): TimelineVerificationRobot {
        return TimelineVerificationRobot(rule).apply(block)
    }
}

class TimelineVerificationRobot(
    private val rule: MainActivityRule
) {

    fun emptyTimelineMessageIsDisplayed() {
        val emptyTimelineMessage = rule.activity.getString(R.string.emptyTimelineMessage)
        rule.onNodeWithText(emptyTimelineMessage)
            .assertIsDisplayed()
    }

    fun postsAreDisplayed(vararg posts: Post) {
        posts.forEach { post ->
            rule.onNodeWithText(post.text)
                .assertIsDisplayed()
        }
    }
}