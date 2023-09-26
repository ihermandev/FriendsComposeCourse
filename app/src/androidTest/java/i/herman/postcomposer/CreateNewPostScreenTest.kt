package i.herman.postcomposer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.ihermandev.sharedtest.infrastructure.ControllableClock
import i.herman.MainActivity
import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.post.PostCatalog
import i.herman.domain.user.InMemoryUserData
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.util.Calendar


class CreateNewPostScreenTest {

    @get:Rule
    val createNewPostRule = createAndroidComposeRule<MainActivity>()

    private val timestampWithTimezoneOffset = Calendar.getInstance()
        .also { it.set(2021, 9, 30, 15, 30) }
        .timeInMillis

    @Test
    fun createNewPost() {
        replaceUserDataWith(InMemoryUserData("jovmitId"))
        replacePostCatalogWith(InMemoryPostCatalog(clock = ControllableClock(timestampWithTimezoneOffset)))

        launchPostComposerFor("jovmit@friends.com", createNewPostRule) {
            typePost("My New Post")
            submit()
        } verify {
            newlyCreatedPostIsShown("jovmitId", "30-10-2021 15:30", "My New Post")
        }
    }

    @Test
    fun createMultiplePost() {
        replaceUserDataWith(InMemoryUserData("jovmitId"))
        replacePostCatalogWith(InMemoryPostCatalog(clock = ControllableClock(timestampWithTimezoneOffset)))

        launchPostComposerFor("jovmit@fiends.com", createNewPostRule) {
            typePost("My First Post")
            submit()
            tapOnCreateNewPost()
            typePost("My Second Post")
            submit()
        } verify {
            newlyCreatedPostIsShown("jovmitId", "30-10-2021 15:30", "My First Post")
            newlyCreatedPostIsShown("jovmitId", "30-10-2021 15:30", "My Second Post")
        }
    }

    @After
    fun tearDown() {
        replacePostCatalogWith(InMemoryPostCatalog())
        replaceUserDataWith(InMemoryUserData(""))
    }

    private fun replacePostCatalogWith(postCatalog: PostCatalog) {
        val module = module {
            single(override = true) { postCatalog }
        }
        loadKoinModules(module)
    }

    private fun replaceUserDataWith(userData: InMemoryUserData) {
        val module = module {
            single(override = true) { userData }
        }
        loadKoinModules(module)
    }
}