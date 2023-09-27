package i.herman.friends

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import i.herman.MainActivity
import i.herman.R
import i.herman.domain.user.Friend
import i.herman.timeline.launchTimelineFor

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchFriends(
    rule: MainActivityRule,
    block: FriendsRobot.() -> Unit
): FriendsRobot {
    launchTimelineFor("email@email.com", "Pas$123.", rule) {}
    return FriendsRobot(rule).apply {
        tapOnFriends()
        block()
    }
}

class FriendsRobot(private val rule: MainActivityRule) {

    fun tapOnFriends() {
        val friends = rule.activity.getString(R.string.friends)
        rule.onNodeWithText(friends)
            .performClick()
    }

    infix fun verify(
        block: FriendsVerificationRobot.() -> Unit
    ): FriendsVerificationRobot {
        return FriendsVerificationRobot(rule).apply(block)
    }
}

class FriendsVerificationRobot(
    private val rule: MainActivityRule
) {

    fun emptyFriendsMessageIsDisplayed() {
        val emptyFriendsMessage = rule.activity.getString(R.string.emptyFriendsMessage)
        rule.onNodeWithText(emptyFriendsMessage)
            .assertIsDisplayed()
    }

    fun friendsAreDisplayed(vararg friends: Friend) {
        friends.forEach { friend ->
            rule.onNodeWithText(friend.user.id)
                .assertIsDisplayed()
        }
    }

    fun friendInformationIsDisplayedFor(friend: Friend) {
        val follow = rule.activity.getString(R.string.follow)
        rule.onNodeWithText(friend.user.id)
            .assertIsDisplayed()
        rule.onNodeWithText(friend.user.about)
            .assertIsDisplayed()
        rule.onNodeWithText(follow)
            .assertIsDisplayed()
    }

    fun loadingIndicatorIsDisplayed() {
        val loading = rule.activity.getString(R.string.loading)
        rule.onNodeWithContentDescription(loading)
            .assertIsDisplayed()
    }

    fun backendErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.fetchingFriendsError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun offlineErrorIsDisplayed() {
        val offlineError = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(offlineError)
            .assertIsDisplayed()
    }
}