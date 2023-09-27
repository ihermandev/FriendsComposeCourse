package i.herman.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.user.Friend
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.domain.user.User
import i.herman.domain.user.UserCatalog
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class FriendsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ana = User("anaId", "ana@friends.com", "something about Anna")
    private val friendAna = Friend(ana, false)
    private val bob = User("bobId", "bob@friends.com", "")
    private val friendBob = Friend(bob, false)
    private val users = mutableMapOf("" to mutableListOf(ana, bob))

    @Test
    fun showsEmptyFriendsMessage() {
        launchFriends(rule) {
            //no operation
        } verify {
            emptyFriendsMessageIsDisplayed()
        }
    }

    @Test
    fun showsLoadingIndicator() {
        val loadFriendsFunction: suspend () -> List<Friend> = {
            delay(1000)
            listOf(friendAna, friendBob)
        }
        replaceUserCatalogWith(ControllableUserCatalog(loadFriendsFunction = loadFriendsFunction))

        launchFriends(rule) {
            //no operation
        } verify {
            loadingIndicatorIsDisplayed()
        }
    }

    @Test
    fun showsAvailableFriends() {
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            //no operation
        } verify {
            friendsAreDisplayed(friendAna, friendBob)
        }
    }

    @Test
    fun showsRequiredFriendInformation() {
        val users = mutableMapOf("" to mutableListOf(ana))
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            //no operation
        } verify {
            friendInformationIsDisplayedFor(friendAna)
        }
    }

    @Test
    fun showsBackendError() {
        val loadFriendsFunction: suspend () -> List<Friend> = { throw BackendException() }
        replaceUserCatalogWith(ControllableUserCatalog(loadFriendsFunction = loadFriendsFunction))

        launchFriends(rule) {
            //no operation
        } verify {
            backendErrorIsDisplayed()
        }
    }

    @Test
    fun showsOfflineError() {
        val loadFriendsFunction: suspend () -> List<Friend> = { throw ConnectionUnavailableException() }
        replaceUserCatalogWith(ControllableUserCatalog(loadFriendsFunction = loadFriendsFunction))

        launchFriends(rule) {
            //no operation
        } verify {
            offlineErrorIsDisplayed()
        }
    }

    @After
    fun tearDown() {
        replaceUserCatalogWith(InMemoryUserCatalog())
    }

    private fun replaceUserCatalogWith(userCatalog: UserCatalog) {
        val replaceModule = module {
            factory { userCatalog }
        }
        loadKoinModules(replaceModule)
    }

    private class ControllableUserCatalog(
        private val followedByFunction: suspend () -> List<String> = { emptyList() },
        private val loadFriendsFunction: suspend () -> List<Friend> = { emptyList() }
    ) : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            return User(":irrelevant:", email, about)
        }

        override suspend fun followedBy(userId: String): List<String> {
            return followedByFunction()
        }

        override suspend fun loadFriendsFor(userId: String): List<Friend> {
            return loadFriendsFunction()
        }
    }
}