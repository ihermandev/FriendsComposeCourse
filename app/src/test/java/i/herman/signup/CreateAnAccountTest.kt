package i.herman.signup

import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.domain.user.User
import i.herman.domain.user.UserRepository
import i.herman.domain.validation.RegexCredentialsValidator
import i.herman.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

    private val credentialsValidator = RegexCredentialsValidator()
    private val inMemoryUserCatalog: InMemoryUserCatalog = InMemoryUserCatalog()
    private val userRepository: UserRepository = UserRepository(inMemoryUserCatalog)
    private val viewModel = SignUpViewModel(credentialsValidator, userRepository, TestDispatchers())

    @Test
    fun accountCreated() {
        val maya = User("mayaId", "maya@friends.com", "about Maya")

        viewModel.createAccount(maya.email, "MaY@2021", maya.about)

        assertEquals(SignUpState.SignedUp(maya), viewModel.signUpState.value)
    }

    @Test
    fun anotherAccountCreated() {
        val bob = User("bobId", "bob@friends.com", "about Bob")

        viewModel.createAccount(bob.email, "Ple@seSubscribe1", bob.about)

        assertEquals(SignUpState.SignedUp(bob), viewModel.signUpState.value)
    }


    @Test
    fun createDuplicateAccount() {
        val anna = User("annaId", "anna@friends.com", "about Anna")
        val password = "AnNaPas$123"
        val usersForPassword = mutableMapOf(password to mutableListOf(anna))
        val userRepository = UserRepository(InMemoryUserCatalog(usersForPassword))
        val viewModel = SignUpViewModel(credentialsValidator, userRepository, TestDispatchers())

        viewModel.createAccount(anna.email, password, anna.about)

        assertEquals(SignUpState.DuplicateAccount, viewModel.signUpState.value)
    }
}