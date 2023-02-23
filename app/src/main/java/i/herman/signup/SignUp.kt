package i.herman.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import i.herman.R

@Composable
@Preview(device = Devices.PIXEL_4)
fun SignUp() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ScreenTitle(R.string.createAnAccount)

        Spacer(modifier = Modifier.height(16.dp))

        EmailField(
            value = email,
            onValueChange = {
                email = it
            })

        PasswordField(
            value = password,
            onValueChange = {
                password = it
            })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            Text(text = stringResource(id = R.string.signUp))
        }

    }
}

@Composable
private fun PasswordField(
    value: String,
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
        modifier = Modifier.fillMaxWidth(),
        value = value,
        trailingIcon = {
            VisibilityToggle(isVisible) {
                isVisible = !isVisible
            }
        },
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(id = R.string.password))
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
            contentDescription = stringResource(id = R.string.toggleVisibility))
    }
}

@Composable
private fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(id = R.string.email))
        })
}

@Composable
private fun ScreenTitle(
    @StringRes titleId: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = titleId),
            style = typography.h4
        )
    }
}