package i.herman.signup.validation

sealed class CredentialsValidationResult {

    object InvalidEmail : CredentialsValidationResult()

    object InvalidPassword : CredentialsValidationResult()

    object Valid : CredentialsValidationResult()
}