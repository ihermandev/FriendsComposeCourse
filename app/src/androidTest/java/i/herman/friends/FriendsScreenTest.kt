package i.herman.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import org.junit.Rule
import org.junit.Test

class FriendsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displaysFriends() {
        launchTimeline(rule) {
            tapOnFriends()
        } verify {
            friendsScreenIsPresent()
        }
    }
}