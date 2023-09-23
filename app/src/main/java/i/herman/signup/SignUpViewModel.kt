package i.herman.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.herman.app.CoroutineDispatchers
import i.herman.domain.user.UserRepository
import i.herman.domain.validation.CredentialsValidationResult
import i.herman.domain.validation.RegexCredentialsValidator
import i.herman.signup.state.SignUpState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val credentialsValidator: RegexCredentialsValidator,
    private val userRepository: UserRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val mutableSignUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = mutableSignUpState

    fun createAccount(
        email: String,
        password: String,
        about: String,
    ) {
        when (credentialsValidator.validate(email, password)) {
            CredentialsValidationResult.InvalidEmail ->
                mutableSignUpState.value = SignUpState.InvalidEmail
            CredentialsValidationResult.InvalidPassword ->
                mutableSignUpState.value = SignUpState.InvalidPassword
            CredentialsValidationResult.Valid ->
                proceedWithSignUp(email, password, about)
        }
    }

    private fun proceedWithSignUp(email: String, password: String, about: String) {
        viewModelScope.launch {
            mutableSignUpState.value = SignUpState.Loading
            mutableSignUpState.value = withContext(dispatchers.background) {
                userRepository.signUp(email, password, about)
            }
        }
    }
}

