package i.herman.app

import i.herman.domain.user.InMemoryUserCatalog
import i.herman.domain.user.UserRepository
import i.herman.domain.validation.RegexCredentialsValidator
import i.herman.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { InMemoryUserCatalog() }
    factory { RegexCredentialsValidator() }
    factory { UserRepository(usersCatalog = get()) }

    viewModel {
        SignUpViewModel(
            credentialsValidator = get(),
            userRepository = get()
        )
    }
}