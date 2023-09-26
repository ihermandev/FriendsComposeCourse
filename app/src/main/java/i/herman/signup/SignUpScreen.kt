package i.herman.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import i.herman.R
import i.herman.signup.state.SignUpScreenState
import i.herman.signup.state.SignUpState
import i.herman.ui.composables.BlockingLoading
import i.herman.ui.composables.InfoMessage
import i.herman.ui.composables.ScreenTitle
import org.koin.androidx.compose.getViewModel


@Composable
fun SignUpScreen(
    onSignedUp: (String) -> Unit,
) {

    val signUpViewModel = getViewModel<SignUpViewModel>()
    val screenState by remember { mutableStateOf(SignUpScreenState()) }
    val signUpState by signUpViewModel.signUpState.observeAsState()

    when (signUpState) {
        is SignUpState.Loading -> screenState.toggleLoading()
        is SignUpState.SignedUp -> onSignedUp((signUpState as SignUpState.SignedUp).user.id)
        is SignUpState.InvalidEmail -> screenState.showBadEmail()
        is SignUpState.InvalidPassword -> screenState.showBadPassword()
        is SignUpState.DuplicateAccount -> screenState.toggleInfoMessage(R.string.duplicateAccountError)
        is SignUpState.BackendError -> screenState.toggleInfoMessage(R.string.createAccountError)
        is SignUpState.Offline -> screenState.toggleInfoMessage(R.string.offlineError)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(R.string.createAnAccount)
            Spacer(modifier = Modifier.height(16.dp))
            EmailField(
                value = screenState.email,
                isError = screenState.showBadEmail,
                onValueChange = { screenState.email = it }
            )
            PasswordField(
                value = screenState.password,
                isError = screenState.showBadPassword,
                onValueChange = { screenState.password = it }
            )
            AboutField(
                value = screenState.about,
                onValueChange = { screenState.about = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    screenState.resetUiState()
                    with(screenState) {
                        signUpViewModel.createAccount(email, password, about)
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.signUp))
            }
        }
        InfoMessage(stringResource = screenState.currentInfoMessage)
        BlockingLoading(screenState.isLoading)
    }
}

@Composable
private fun PasswordField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    var isVisible by remember {
        mutableStateOf(false)
    }

    val visualTransformation = if (isVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.password)),
        value = value,
        isError = isError,
        trailingIcon = {
            VisibilityToggle(isVisible) {
                isVisible = !isVisible
            }
        },
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        label = {
            val resource = if (isError) R.string.badPasswordError else R.string.password
            Text(text = stringResource(id = resource))
        })
}

@Composable
private fun VisibilityToggle(
    isVisible: Boolean,
    onToggle: () -> Unit,
) {
    val visibilityIcon = if (isVisible) {
        painterResource(id = R.drawable.ic_visible_eye)
    } else {
        painterResource(id = R.drawable.ic_invisible_eye)
    }

    IconButton(
        onClick = { onToggle() }
    ) {
        Icon(
            painter = visibilityIcon,
            contentDescription = stringResource(id = R.string.toggleVisibility)
        )
    }
}

@Composable
private fun EmailField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.email)),
        value = value,
        isError = isError,
        onValueChange = onValueChange,
        label = {
            val resource = if (isError) R.string.badEmailError else R.string.email
            Text(text = stringResource(id = resource))
        })
}

@Composable
fun AboutField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = {
            Text(text = stringResource(id = R.string.about))
        },
        onValueChange = onValueChange
    )
}