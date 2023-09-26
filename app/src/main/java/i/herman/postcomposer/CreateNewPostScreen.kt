package i.herman.postcomposer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import i.herman.postcomposer.state.CreatePostState
import i.herman.ui.composables.ScreenTitle
import i.herman.R
import i.herman.postcomposer.state.CreateNewPostScreenState
import i.herman.ui.composables.BlockingLoading
import i.herman.ui.composables.InfoMessage

@Composable
fun CreateNewPostScreen(
    createPostViewModel: CreatePostViewModel,
    onPostCreated: () -> Unit
) {

    val screenState by remember { mutableStateOf(CreateNewPostScreenState()) }
    var postText by remember { mutableStateOf("") }

    val createPostState by createPostViewModel.postState.observeAsState()

    when (createPostState) {
        is CreatePostState.Loading ->
            screenState.showLoading()
        is CreatePostState.Created -> {
            if (screenState.isPostSubmitted) {
                onPostCreated()
            }
        }
        is CreatePostState.BackendError ->
            screenState.showMessage(R.string.creatingPostError)
        is CreatePostState.Offline ->
            screenState.showMessage(R.string.offlineError)
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(resource = R.string.createNewPost)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                PostComposer(postText) { postText = it }
                FloatingActionButton(
                    onClick = {
                        screenState.setPostSubmitted()
                        createPostViewModel.createPost(postText)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .testTag(stringResource(id = R.string.submitPost))
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(id = R.string.submitPost)
                    )
                }
            }
        }
        InfoMessage(stringResource = screenState.currentMessage)
        BlockingLoading(isShowing = screenState.isLoading)
    }
}

@Composable
private fun PostComposer(
    postText: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = postText,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.newPostHint)) }
    )
}