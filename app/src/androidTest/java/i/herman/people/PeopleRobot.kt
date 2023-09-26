package i.herman.people

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import i.herman.MainActivity
import i.herman.timeline.launchTimelineFor
import i.herman.R

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchTimeline(
    rule: MainActivityRule,
    block: PeopleRobot.() -> Unit
): PeopleRobot {
    launchTimelineFor("email@email.com", "Pas$123.", rule) {}
    return PeopleRobot(rule).apply(block)
}

class PeopleRobot(private val rule: MainActivityRule) {

    fun tapOnPeople() {
        val people = rule.activity.getString(R.string.people)
        rule.onNodeWithText(people)
            .performClick()
    }

    infix fun verify(
        block: PeopleVerificationRobot.() -> Unit
    ): PeopleVerificationRobot {
        return PeopleVerificationRobot(rule).apply(block)
    }
}

class PeopleVerificationRobot(
    private val rule: MainActivityRule
) {

    fun peopleScreenIsPresent() {
        val people = rule.activity.getString(R.string.people)
        rule.onAllNodesWithText(people)
            .onFirst()
            .assertIsDisplayed()
    }
}