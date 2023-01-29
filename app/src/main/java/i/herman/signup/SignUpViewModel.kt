package i.herman.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.signup.state.SignUpState
import i.herman.signup.validation.CredentialsValidationResult
import i.herman.signup.validation.RegexCredentialsValidator

class SignUpViewModel {

    private val _mutableSignUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _mutableSignUpState

    private val credentialsValidator =  RegexCredentialsValidator()

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
            CredentialsValidationResult.Valid -> TODO()
        }
    }
}

