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

    private val userRepository = UserRepository(InMemoryUserCatalog())

    fun createAccount(
        email: String,
        password: String,
        about: String,
    ) {
        when (credentialsValidator.validate(email, password)) {
            CredentialsValidationResult.InvalidEmail ->
                _mutableSignUpState.value = SignUpState.InvalidEmail
            CredentialsValidationResult.InvalidPassword ->
                _mutableSignUpState.value = SignUpState.InvalidPassword
            CredentialsValidationResult.Valid ->
                _mutableSignUpState.value = userRepository.signUp(email, about, password)
        }
    }

    class UserRepository(private val usersCatalog: InMemoryUserCatalog) {

        fun signUp(
            email: String,
            about: String,
            password: String,
        ): SignUpState {
            return try {
                val user = usersCatalog.createUser(email, about, password)
                SignUpState.SignedUp(user)
            } catch (duplicateAccount: DuplicateAccountException) {
                SignUpState.DuplicateAccount
            }
        }
    }

    class InMemoryUserCatalog(
        private val usersForPassword: MutableMap<String, MutableList<User>> = mutableMapOf(),
    ) {

        fun createUser(
            email: String,
            about: String,
            password: String,
        ): User {
            checkAccountExist(email)
            val userId = createUserIdFor(email)
            val user = User(userId, email, about)
            saveUser(password, user)
            return user
        }

        private fun saveUser(password: String, user: User) {
            usersForPassword.getOrPut(password) { mutableListOf() }.add(user)
        }

        private fun createUserIdFor(email: String): String {
            return email.takeWhile { it != '@' } + "Id"
        }

        private fun checkAccountExist(email: String) {
            if (usersForPassword.values.flatten().any { it.email == email }
            ) {
                throw DuplicateAccountException()
            }
        }
    }

}

class DuplicateAccountException : Throwable() {

}

