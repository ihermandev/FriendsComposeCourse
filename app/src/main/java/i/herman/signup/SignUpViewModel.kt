package i.herman.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.signup.state.SignUpState

class SignUpViewModel {

    private val _mutableSignUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _mutableSignUpState

    fun createAccount(
        email: String,
        password: String,
        about: String,
    ) {
        _mutableSignUpState.value = SignUpState.InvalidEmail
    }
}
