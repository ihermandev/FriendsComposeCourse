package i.herman.signup

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import i.herman.domain.user.InMemoryUserCatalog
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class SignUpTest {

    @get: Rule
    val signUpTestRule = createAndroidComposeRule<MainActivity>()

    private val userCatalog = InMemoryUserCatalog()
    private val signUpModule = module {
        factory(override = true) { userCatalog }
    }

    @Before
    fun setUp() {
        loadKoinModules(signUpModule)
    }

    @Test
    fun performSignUp() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("izzi@friends.app")
            typePassword("PassW0rd!")
            submit()
        } verify {
            timelineScreenIsPresent()
        }
    }

    @Test
    fun displayDuplicateAccountError() {
        val signedUpUserEmail = "alice@friends.com"
        val signedUpUserPassword = "@l1cePass"
        createUserWith(signedUpUserEmail, signedUpUserPassword)

        launchSignUpScreen(signUpTestRule) {
            typeEmail(signedUpUserEmail)
            typePassword(signedUpUserPassword)
            submit()
        } verify {
            duplicateAccountErrorIsShown()
        }
    }

    @After
    fun tearDown() {
        val resetModule = module {
            single(override = true) { InMemoryUserCatalog() }
        }
        loadKoinModules(resetModule)
    }

    private fun createUserWith(
        signedUpUserEmail: String,
        signedUpUserPassword: String
    ) {
        userCatalog.createUser(signedUpUserEmail, signedUpUserPassword, "")
    }
}