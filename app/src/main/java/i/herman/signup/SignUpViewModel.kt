package i.herman.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.herman.domain.user.UserRepository
import i.herman.domain.validation.CredentialsValidationResult
import i.herman.domain.validation.RegexCredentialsValidator
import i.herman.signup.state.SignUpState

class SignUpViewModel(
    private val credentialsValidator: RegexCredentialsValidator,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _mutableSignUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _mutableSignUpState

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
}

