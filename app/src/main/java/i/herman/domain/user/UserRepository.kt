package i.herman.domain.user

import i.herman.domain.exceptions.BackendException
import i.herman.domain.exceptions.ConnectionUnavailableException
import i.herman.domain.exceptions.DuplicateAccountException
import i.herman.signup.state.SignUpState

class UserRepository(
    private val userCatalog: UserCatalog,
    private val userDataStore: UserDataStore
) {

    suspend fun signUp(
        email: String,
        password: String,
        about: String
    ): SignUpState {
        return try {
            val user = userCatalog.createUser(email, password, about)
            userDataStore.storeLoggedInUserId(user.id)
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