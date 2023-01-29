package i.herman.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import i.herman.signup.state.SignUpState
import java.util.regex.Pattern

class SignUpViewModel {

    private val _mutableSignUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _mutableSignUpState

    fun createAccount(
        email: String,
        password: String,
        about: String,
    ) {
        val emailRegex =
            """[a-zA-Z0-9+._%\-]{1,64}@[a-zA-Z0-9][a-zA-Z0-9\-]{1,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{1,25})"""
        val emailPattern = Pattern.compile(emailRegex)

        val passwordRegex = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=/\-]).{8,}$"""
        val passwordPattern = Pattern.compile(passwordRegex)

        if (!emailPattern.matcher(email).matches()) {
            _mutableSignUpState.value = SignUpState.InvalidEmail
        } else if (!passwordPattern.matcher(password).matches()) {
            _mutableSignUpState.value = SignUpState.InvalidPassword
        }
    }
}
