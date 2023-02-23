package i.herman.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.signup.domain.user.User
import i.herman.signup.domain.validation.CredentialsValidationResult
import i.herman.signup.domain.validation.RegexCredentialsValidator
import i.herman.signup.state.SignUpState

class SignUpViewModel(
    private val credentialsValidator: RegexCredentialsValidator,
) {

    private val _mutableSignUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _mutableSignUpState

    private val usersForPassword = mutableMapOf<String, MutableList<User>>()

    fun createAccount(
        email: String,
        password: String,
        about: String,
    ) {
        when (credentialsValidator.validate(email, password)) {
            CredentialsValidationResult.InvalidEmail -> _mutableSignUpState.value =
                SignUpState.InvalidEmail
            CredentialsValidationResult.InvalidPassword -> _mutableSignUpState.value =
                SignUpState.InvalidPassword
            CredentialsValidationResult.Valid -> {
                try {
                    val user = createUser(email, about, password)
                    _mutableSignUpState.value = SignUpState.SignedUp(user)
                } catch (duplicateAccount: DuplicateAccountException) {
                    _mutableSignUpState.value = SignUpState.DuplicateAccount
                }
            }
        }
    }

    private fun createUser(
        email: String,
        about: String,
        password: String,
    ): User {
        if (usersForPassword.values.flatten().any { it.email == email }
        ) {
            throw DuplicateAccountException()
        }
        val userId = email.takeWhile { it != '@' } + "Id"
        val user = User(userId, email, about)
        usersForPassword.getOrPut(password) { mutableListOf() }.add(user)
        return user
    }
}

class DuplicateAccountException : Throwable() {

}

