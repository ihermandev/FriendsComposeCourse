package i.herman.signup.state

import i.herman.domain.user.User

sealed class SignUpState {

    object Loading : SignUpState()

    data class SignedUp(val user: User) : SignUpState()

    object InvalidEmail : SignUpState()

    object InvalidPassword : SignUpState()

    object DuplicateAccount : SignUpState()

    object BackendError : SignUpState()

    object Offline : SignUpState()
}