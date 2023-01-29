package i.herman.signup.state

sealed class SignUpState {

    object InvalidEmail : SignUpState()

    object InvalidPassword : SignUpState()
}