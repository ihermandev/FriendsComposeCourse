package i.herman.signup

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import org.junit.Rule
import org.junit.Test

class SignUpTest {

    @get: Rule
    val signUpTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun performSignUp() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("izzi@friends.app")
            typePassword("password")
            submit()
        } verify {
            timelineScreenIsPresent()
        }
    }
}