package i.herman.signup

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.ihermandev.sharedtest.domain.user.OfflineUserCatalog
import com.ihermandev.sharedtest.domain.user.UnavailableUserCatalog
import i.herman.MainActivity
import i.herman.domain.user.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class SignUpScreenTest {

    @get: Rule
    val signUpTestRule = createAndroidComposeRule<MainActivity>()

    private val signUpModule = module {
        factory<UserCatalog>(override = true) { InMemoryUserCatalog() }
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
    fun displayBadEmailError() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("email")
            submit()
        } verify {
            badEmailErrorIsShown()
        }
    }

    @Test
    fun resetBadEmailError() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("email")
            submit()
            typeEmail("email@")
        } verify {
            badEmailErrorIsNotShown()
        }
    }

    @Test
    fun displayBadPasswordError() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("jov@friends.com")
            typePassword("abc")
            submit()
        } verify {
            badPasswordErrorIsShown()
        }
    }

    @Test
    fun resetBadPasswordError() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("valid@email.com")
            typePassword("ads")
            submit()
            typePassword("newTry")
        } verify {
            badPasswordErrorIsNotShown()
        }
    }

    @Test
    fun displayDuplicateAccountError() = runBlocking<Unit> {
        val signedUpUserEmail = "alice@friends.com"
        val signedUpUserPassword = "@l1cePass"
        replaceUserCatalogWith(InMemoryUserCatalog().apply {
            createUser(signedUpUserEmail, signedUpUserPassword, "")
        })

        launchSignUpScreen(signUpTestRule) {
            typeEmail(signedUpUserEmail)
            typePassword(signedUpUserPassword)
            submit()
        } verify {
            duplicateAccountErrorIsShown()
        }
    }

    @Test
    fun displayBackendError() {
        replaceUserCatalogWith(UnavailableUserCatalog())

        launchSignUpScreen(signUpTestRule) {
            typeEmail("joe@friends.com")
            typePassword("Jo3PassWord#@")
            submit()
        } verify {
            backendErrorIsShown()
        }
    }

    @Test
    fun displayOfflineError() {
        replaceUserCatalogWith(OfflineUserCatalog())

        launchSignUpScreen(signUpTestRule) {
            typeEmail("joe@friends.com")
            typePassword("Jo3PassWord#@")
            submit()
        } verify {
            offlineErrorIsShown()
        }
    }

    @Test
    fun displayBlockingLoading() {
        replaceUserCatalogWith(DelayingUserCatalog())
        launchSignUpScreen(signUpTestRule) {
            typeEmail("caly@friends.com")
            typePassword("C@lyP1ss#")
            submit()
        } verify {
            blockingLoadingIsShown()
        }
    }

    @After
    fun tearDown() {
        replaceUserCatalogWith(InMemoryUserCatalog())
    }

    private fun replaceUserCatalogWith(userCatalog: UserCatalog) {
        val replaceModule = module {
            factory(override = true) { userCatalog }
        }
        loadKoinModules(replaceModule)
    }

    class DelayingUserCatalog : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            delay(1000)
            return User("someId", email, about)
        }

        override fun followedBy(userId: String): List<String> {
            TODO("Not yet implemented")
        }
    }
}