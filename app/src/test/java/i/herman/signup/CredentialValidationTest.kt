package i.herman.signup

import i.herman.InstantTaskExecutorExtension
import i.herman.signup.domain.validation.CredentialsValidationResult
import i.herman.signup.domain.validation.RegexCredentialsValidator
import i.herman.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialValidationTest {

    private val regexCredentialsValidator: RegexCredentialsValidator by lazy {
        RegexCredentialsValidator()
    }

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
        val viewModel = SignUpViewModel(regexCredentialsValidator)

        viewModel.createAccount(email, ":password:", ":about:")

        assertEquals(SignUpState.InvalidEmail, viewModel.signUpState.value)
    }

    @ParameterizedTest
    @CsvSource(
        "''",
        "'    '",
        "'123'",
        "'1234'",
        "'ABCDF'",
        "'QWERTY'",
    )
    fun invalidPassword(password: String) {
        val viewModel = SignUpViewModel(regexCredentialsValidator)

        viewModel.createAccount("ann@friends.com", password, ":about:")

        assertEquals(SignUpState.InvalidPassword, viewModel.signUpState.value)
    }

    @Test
    fun validCredentials() {
        val result = regexCredentialsValidator.validate("ann@friends.com", "12ABcd3!^")

        assertEquals(CredentialsValidationResult.Valid, result)
    }
}