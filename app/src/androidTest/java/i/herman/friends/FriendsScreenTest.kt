package i.herman.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.friends.ToggleFollowing
import i.herman.domain.user.ControllableUserCatalog
import i.herman.domain.user.Following
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
        val delayedFriendsLoad: suspend () -> List<Friend> = {
            delay(1000)
            listOf(friendAna, friendBob)
        }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = delayedFriendsLoad))

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
        val friendsLoad: suspend () -> List<Friend> = { throw BackendException() }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(rule) {
            //no operation
        } verify {
            backendErrorIsDisplayed()
        }
    }

    @Test
    fun showsOfflineError() {
        val friendsLoad: suspend () -> List<Friend> = { throw ConnectionUnavailableException() }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(rule) {
            //no operation
        } verify {
            offlineErrorIsDisplayed()
        }
    }

    @Test
    fun followAFriend() {
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            tapOnFollowFor(friendAna)
        } verify {
            followingIsAddedFor(friendAna)
        }
    }

    @Test
    fun unfollowAFriend() {
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            tapOnFollowFor(friendBob)
            tapOnUnfollowFor(friendBob)
        } verify {
            followingIsRemovedFor(friendBob)
        }
    }

    @Test
    fun showsLoadingIndicatorWhenUpdatingFriendship() {
        val friendsLoad: suspend () -> List<Friend> = { listOf(friendAna, friendBob) }
        val toggleFollow: suspend (String, String) -> ToggleFollowing = { userId, followingId ->
            delay(1000)
            ToggleFollowing(Following(userId, followingId), true)
        }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad, followToggle = toggleFollow))

        launchFriends(rule) {
            tapOnFollowFor(friendAna)
        } verify {
            loadingIndicatorIsShownWhenTogglingFriendshipFor(friendAna)
        }
    }

    @Test
    fun showsBackendErrorWhenUpdatingFollowing() {
        val friendsLoad: suspend () -> List<Friend> = { listOf(friendAna, friendBob) }
        val toggleFollow: suspend (String, String) -> ToggleFollowing = { _, _ ->
            throw BackendException()
        }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad, followToggle = toggleFollow))

        launchFriends(rule) {
            tapOnFollowFor(friendAna)
        } verify {
            backendErrorFollowingFriendIsDisplayed()
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
}