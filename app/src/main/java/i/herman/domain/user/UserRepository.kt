package i.herman.domain.user

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.exceptions.DuplicateAccountException
import i.herman.signup.state.SignUpState

class UserRepository(private val usersCatalog: UserCatalog) {

    suspend fun signUp(
        email: String,
        about: String,
        password: String,
    ): SignUpState {
        return try {
            val user = usersCatalog.createUser(email, about, password)
            SignUpState.SignedUp(user)
        } catch (duplicateAccount: DuplicateAccountException) {
            SignUpState.DuplicateAccount
        } catch (backendException: BackendException) {
            SignUpState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            SignUpState.Offline
        }

    }
}