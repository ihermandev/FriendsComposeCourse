package i.herman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import i.herman.postcomposer.CreateNewPostScreen
import i.herman.signup.SignUpScreen
import i.herman.timeline.TimelineScreen
import i.herman.ui.theme.FriendsComposeCourseTheme

class MainActivity : ComponentActivity() {

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
                            SignUpScreen { signedUpUserId ->
                                navController.navigate("$TIMELINE/$signedUpUserId") {
                                    popUpTo(SIGN_UP) { inclusive = true }
                                }
                            }
                        }
                        composable(route = "$TIMELINE/{userId}") { backStackEntry ->
                            TimelineScreen(
                                userId = backStackEntry.arguments?.getString("userId") ?: ""
                            ) { navController.navigate(CREATE_NEW_POST) }
                        }
                        composable(CREATE_NEW_POST) {
                            CreateNewPostScreen {
                                navController.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
}