package i.herman.signup

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.user.User
import i.herman.domain.user.UserCatalog
import i.herman.domain.user.UserRepository
import i.herman.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FailedAccountCreationTest {

    @Test
    fun backendError() {
        val userRepository = UserRepository(UnavailableUserCatalog())

        val result = userRepository.signUp(":email:", ":password:", ":about:")

        assertEquals(SignUpState.BackendError, result)
    }

    @Test
    fun offlineError() {
        val userRepository = UserRepository(OfflineUserCatalog())

        val result = userRepository.signUp(":email:", ":password:", ":about:")

        assertEquals(SignUpState.Offline, result)
    }

    class OfflineUserCatalog : UserCatalog {

        override fun createUser(email: String, password: String, about: String): User {
            throw ConnectionUnavailableException()
        }
    }

    class UnavailableUserCatalog : UserCatalog {

        override fun createUser(email: String, password: String, about: String): User {
            throw BackendException()
        }
    }
}