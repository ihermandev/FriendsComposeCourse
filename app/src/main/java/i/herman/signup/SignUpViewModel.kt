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
                if (email.contains(("bob"))) {
                    val user = User("bobId", "bob@friends.com", "about Bob")
                    _mutableSignUpState.value = SignUpState.SignedUp(user)
                } else {
                    val user = User("mayaId", "maya@friends.com", "about Maya")
                    _mutableSignUpState.value = SignUpState.SignedUp(user)
                }
            }
        }
    }
}

