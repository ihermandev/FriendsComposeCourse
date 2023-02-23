package i.herman.signup

import i.herman.InstantTaskExecutorExtension
import i.herman.signup.domain.user.User
import i.herman.signup.domain.validation.RegexCredentialsValidator
import i.herman.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

    private val credentialsValidator = RegexCredentialsValidator()
    private val viewModel = SignUpViewModel(credentialsValidator)

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
}