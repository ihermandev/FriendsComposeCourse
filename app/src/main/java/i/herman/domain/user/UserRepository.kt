package i.herman.domain.user

import i.herman.domain.exceptions.DuplicateAccountException
import i.herman.signup.state.SignUpState

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