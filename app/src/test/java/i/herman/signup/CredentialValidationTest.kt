package i.herman.signup

import i.herman.InstantTaskExecutorExtension
import i.herman.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialValidationTest {

    @Test
    fun invalidEmail() {
        val viewModel = SignUpViewModel()

        viewModel.createAccount("email", ":password:", ":about:")

        assertEquals(SignUpState.InvalidEmail, viewModel.signUpState.value)
    }
}