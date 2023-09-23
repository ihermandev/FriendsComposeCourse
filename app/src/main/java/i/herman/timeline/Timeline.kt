package i.herman.timeline

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import i.herman.R

@Composable
fun TimelineScreen() {
    Text(text = stringResource(id = R.string.timeline))
}