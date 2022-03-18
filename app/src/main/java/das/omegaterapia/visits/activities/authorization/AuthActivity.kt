package das.omegaterapia.visits.activities.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.NotificationID
import das.omegaterapia.visits.R
import das.omegaterapia.visits.activities.authorization.composables.AuthScreen
import das.omegaterapia.visits.activities.main.MainActivity
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.rememberWindowSizeClass
import java.util.concurrent.Executor

@AndroidEntryPoint
class AuthActivity : FragmentActivity() {

    /*--------------------------------------------------
    |            Activity Lifecycle Methods            |
    --------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos la autenticación biométrica
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, authenticationCallback)

        setContent {
            val windowSizeClass = rememberWindowSizeClass()

            OmegaterapiaTheme {
                AuthScreen(windowSizeFormatClass = windowSizeClass, onSuccessfulLogin = this::openMainScreen, onSuccessfulSignIn = this::onSuccessfulSignIn)
            }
        }
    }


    /*-------------------------------------------------
    |              Login Sign In actions              |
    -------------------------------------------------*/

    private fun onSuccessfulSignIn(username: String){
        val builder = NotificationCompat.Builder(this, "AUTH_CHANNEL")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("User created")
            .setContentText("The User $username has been successfully created.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NotificationID.USER_CREATED.id, builder.build())
        }

        openMainScreen(username)
    }

    private fun openMainScreen(username: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("LOGED_USERNAME", username)
        }
        startActivity(intent)
        finish()
    }


    /*--------------------------------------------------
    |                    BIOMETRICS                    |
    --------------------------------------------------*/
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt


    private val authenticationCallback: BiometricPrompt.AuthenticationCallback =
        // Con "object" definimos un singleton que hereda de BiometricPrompt.AuthenticationCallback
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                // TODO
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication Failed. Try again.", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }
        }


    private val biometricPromptInfo: BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Allow Biometric Authentication")
            .setSubtitle("You will no longer required username and password during login")
            .setDescription("We use biometric authentication to protect your data")
            .setNegativeButtonText("Cancel")
            .build()
}

