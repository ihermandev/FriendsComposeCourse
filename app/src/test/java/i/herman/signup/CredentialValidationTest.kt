package i.herman.signup

import i.herman.InstantTaskExecutorExtension
import i.herman.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialValidationTest {

    @ParameterizedTest
    @CsvSource(
        "'email'",
        "'a@b.c'",
        "'ab@b.c'",
        "'ab@bc.c'",
        "''",
        "'      '"
    )
    fun invalidEmail(email: String) {
        val viewModel = SignUpViewModel()

        viewModel.createAccount(email, ":password:", ":about:")

        assertEquals(SignUpState.InvalidEmail, viewModel.signUpState.value)
    }
}