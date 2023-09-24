package i.herman.signup

import i.herman.domain.user.OfflineUserCatalog
import i.herman.domain.user.UnavailableUserCatalog
import i.herman.domain.user.UserRepository
import i.herman.signup.state.SignUpState
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FailedAccountCreationTest {

    @Test
    fun backendError() = runBlocking {
        val userRepository = UserRepository(UnavailableUserCatalog())

        val result = userRepository.signUp(":email:", ":password:", ":about:")

        assertEquals(SignUpState.BackendError, result)
    }

    @Test
    fun offlineError() = runBlocking {
        val userRepository = UserRepository(OfflineUserCatalog())

        val result = userRepository.signUp(":email:", ":password:", ":about:")

        assertEquals(SignUpState.Offline, result)
    }
}