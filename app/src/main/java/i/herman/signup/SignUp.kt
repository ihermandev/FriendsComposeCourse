package i.herman.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import i.herman.R

@Composable
@Preview(device = Devices.PIXEL_4)
fun SignUp() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Create an account")

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(text = stringResource(id = R.string.email))
            })

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = {
                Text(text = stringResource(id = R.string.password))
            })

        Button(onClick = {}) {
            Text(text = stringResource(id = R.string.signUp))
        }

    }
}