package das.omegaterapia.visits.activities.authorization


import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.NotificationID
import das.omegaterapia.visits.R
import das.omegaterapia.visits.activities.authorization.composables.screens.AnimatedSplashScreen
import das.omegaterapia.visits.activities.authorization.composables.screens.AuthScreen
import das.omegaterapia.visits.activities.main.MainActivity
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.rememberWindowSizeClass

@AndroidEntryPoint
class AuthActivity : FragmentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var biometricAuthManager: BiometricAuthManager


    /*--------------------------------------------------
    |            Activity Lifecycle Methods            |
    --------------------------------------------------*/
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos la autenticación biométrica
        biometricAuthManager =
            BiometricAuthManager(
                context = this,
                authUser = authViewModel.lastLoggedUser ?: "",
                onAuthenticationSucceeded = this::onSuccessfulLogin
            )

        setContent {
            OmegaterapiaTheme {
                val windowSizeClass = rememberWindowSizeClass()

                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = "splash_screen"
                ) {
                    composable(
                        route = "splash_screen",
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
                        AnimatedSplashScreen {
                            navController.popBackStack()
                            navController.navigate("auth_screen")
                        }
                    }
                    composable(
                        route = "auth_screen",
                        enterTransition = { fadeIn(animationSpec = tween(500)) }
                    ) {
                        AuthScreen(
                            authViewModel = authViewModel,
                            windowSizeFormatClass = windowSizeClass,
                            biometricSupportChecker = biometricAuthManager::checkBiometricSupport,
                            onSuccessfulLogin = ::onSuccessfulLogin,
                            onSuccessfulSignIn = ::onSuccessfulSignIn,
                            onSuccessfulBiometricLogin = biometricAuthManager::submitBiometricAuthorization
                        )
                    }
                }
            }

        }
    }


    /*-------------------------------------------------
    |              Login Sign In actions              |
    -------------------------------------------------*/

    private fun onSuccessfulSignIn(username: String) {

        // Show user created notification
        val builder = NotificationCompat.Builder(this, "AUTH_CHANNEL")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("User created")
            .setContentText("The User $username has been successfully created.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NotificationID.USER_CREATED.id, builder.build())
        }

        // Log the new user
        onSuccessfulLogin(username)
    }

    private fun onSuccessfulLogin(username: String) {
        // Set the last logged user
        authViewModel.updateLastLoggedUsername(username)

        // Open the amin activity
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("LOGED_USERNAME", username)
        }
        startActivity(intent)
        finish()
    }
}