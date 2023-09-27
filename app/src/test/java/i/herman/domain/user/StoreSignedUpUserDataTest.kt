package i.herman.domain.user

import i.herman.domain.friends.ToggleFollowing
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StoreSignedUpUserDataTest {

    @Test
    fun successSigningUp() = runBlocking {
        val userDataStore = InMemoryUserDataStore()
        val userRepository = UserRepository(UserCatalogCreatingUsersWith("userId"), userDataStore)

        userRepository.signUp(":email:", ":password:", ":about:")

        assertEquals("userId", userDataStore.loggedInUserId())
    }

    private class UserCatalogCreatingUsersWith(
        private val desiredUserId: String
    ) : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            return User(desiredUserId, email, about)
        }

        override fun toggleFollowing(userId: String, followerId: String): ToggleFollowing {
            TODO("Not yet implemented")
        }

        override suspend fun followedBy(userId: String): List<String> {
            TODO("Not yet implemented")
        }

        override suspend fun loadFriendsFor(userId: String): List<Friend> {
            TODO("Not yet implemented")
        }
    }
}