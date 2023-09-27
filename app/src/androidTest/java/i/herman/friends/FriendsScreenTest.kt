package i.herman.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import i.herman.MainActivity
import i.herman.domain.user.Friend
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.domain.user.User
import i.herman.domain.user.UserCatalog
import org.junit.After
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class FriendsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    @Ignore("Waiting for the BL update")
    fun showsEmptyFriendsMessage() {
        launchFriends(rule) {
            //no operation
        } verify {
            emptyFriendsMessageIsDisplayed()
        }
    }

    @Test
    fun showsAvailableFriends() {
        val ana = User("anaId", "ana@friends.com", "")
        val friendAna = Friend(ana, false)
        val bob = User("bobId", "bob@friends.com", "")
        val friendBob = Friend(bob, false)
        val users = mutableMapOf("" to mutableListOf(ana, bob))
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            //no operation
        } verify {
            friendsAreDisplayed(friendAna, friendBob)
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