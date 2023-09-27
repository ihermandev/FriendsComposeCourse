package i.herman.signup

import androidx.lifecycle.SavedStateHandle
import i.herman.InstantTaskExecutorExtension
import i.herman.app.TestDispatchers
import i.herman.domain.user.InMemoryUserCatalog
import i.herman.domain.user.InMemoryUserDataStore
import i.herman.domain.user.User
import i.herman.domain.user.UserRepository
import i.herman.domain.validation.RegexCredentialsValidator
import i.herman.signup.state.SignUpScreenState
import i.herman.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingSignUpStatesTest {

    private val userRepository = UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore())
    private val viewModel = SignUpViewModel(
        RegexCredentialsValidator(),
        userRepository,
        SavedStateHandle(),
        TestDispatchers()
    )
    private val tom = User("tomId", "tom@friends.com", "about Tom")

    @Test
    fun uiStatesAreDeliveredInParticularOrder() {
        val deliveredStates = mutableListOf<SignUpScreenState>()
        viewModel.screenState.observeForever { deliveredStates.add(it) }

        viewModel.createAccount(tom.email, "P@ssWord1#$", tom.about)

        assertEquals(
            listOf(SignUpScreenState(isLoading = true), SignUpScreenState(signedUpUserId = tom.id)),
            deliveredStates
        )
    }
}