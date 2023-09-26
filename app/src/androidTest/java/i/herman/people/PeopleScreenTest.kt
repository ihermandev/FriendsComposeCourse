package i.herman.people

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import org.junit.Rule
import org.junit.Test

class PeopleScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displaysPeople() {
        launchTimeline(rule) {
            tapOnPeople()
        } verify {
            peopleScreenIsPresent()
        }
    }
}