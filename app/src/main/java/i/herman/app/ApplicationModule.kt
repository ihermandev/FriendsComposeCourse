package i.herman.app

import i.herman.domain.post.InMemoryPostCatalog
import i.herman.domain.post.PostCatalog
import i.herman.domain.post.PostRepository
import i.herman.domain.timeline.TimelineRepository
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.domain.user.InMemoryUserDataStore
import i.herman.domain.user.UserCatalog
import i.herman.domain.user.UserDataStore
import i.herman.domain.user.UserRepository
import i.herman.domain.validation.RegexCredentialsValidator
import i.herman.postcomposer.CreatePostViewModel
import i.herman.signup.SignUpViewModel
import i.herman.timeline.TimelineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single<CoroutineDispatchers> { DefaultDispatchers() }
    single<UserCatalog> { InMemoryUserCatalog() }
    single<PostCatalog> { InMemoryPostCatalog() }
    single<UserDataStore> { InMemoryUserDataStore() }
    factory { RegexCredentialsValidator() }
    factory { UserRepository(userCatalog = get(), userDataStore = get()) }
    factory { TimelineRepository(userCatalog = get(), postCatalog = get()) }
    factory { PostRepository(userDataStore = get(), postCatalog = get()) }

    viewModel {
        SignUpViewModel(
            credentialsValidator = get(),
            userRepository = get(),
            dispatchers = get()
        )
    }

    viewModel {
        TimelineViewModel(timelineRepository = get(), dispatchers = get())
    }

    viewModel { CreatePostViewModel(postRepository = get(), dispatchers = get()) }
}