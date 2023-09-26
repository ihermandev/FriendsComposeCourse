package i.herman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import i.herman.home.HomeScreen
import i.herman.navigation.Screen
import i.herman.signup.SignUpScreen
import i.herman.ui.theme.FriendsComposeCourseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FriendsComposeCourseTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainApp()
                }
            }
        }
    }

    @Composable
    private fun MainApp() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.SignUp.route) {
            composable(Screen.SignUp.route) {
                SignUpScreen { signedUpUserId ->
                    navController.navigate(Screen.Home.createRoute(signedUpUserId)) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            }
            composable(route = Screen.Home.route) { backStackEntry ->
                HomeScreen(userId = backStackEntry.arguments?.getString(Screen.Home.userId) ?: "")
            }
        }
    }
}