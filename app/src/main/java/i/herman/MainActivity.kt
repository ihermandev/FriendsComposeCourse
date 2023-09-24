package i.herman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import i.herman.postcomposer.CreateNewPostScreen
import i.herman.postcomposer.CreatePostViewModel
import i.herman.signup.SignUpScreen
import i.herman.signup.SignUpViewModel
import i.herman.timeline.TimelineScreen
import i.herman.timeline.TimelineViewModel
import i.herman.ui.theme.FriendsComposeCourseTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val signUpViewModel: SignUpViewModel by viewModel()
    private val timelineViewModel: TimelineViewModel by viewModel()
    private val createPostViewModel: CreatePostViewModel by viewModel()

    private companion object {
        private const val SIGN_UP = "signUp"
        private const val TIMELINE = "timeline"
        private const val CREATE_NEW_POST = "createNewPost"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            FriendsComposeCourseTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = SIGN_UP) {
                        composable(SIGN_UP) {
                            SignUpScreen(signUpViewModel) { signedUpUserId ->
                                navController.navigate("$TIMELINE/$signedUpUserId") {
                                    popUpTo(SIGN_UP) { inclusive = true }
                                }
                            }
                        }
                        composable(
                            route = "$TIMELINE/{userId}",
                            arguments = listOf(navArgument("userId") { })
                        ) { backStackEntry ->
                            TimelineScreen(
                                userId = backStackEntry.arguments?.getString("userId") ?: "",
                                timelineViewModel = timelineViewModel,
                                onCreateNewPost = { navController.navigate(CREATE_NEW_POST) }
                            )
                        }
                        composable(CREATE_NEW_POST) {
                            CreateNewPostScreen(createPostViewModel) {
                                navController.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
}