package das.omegaterapia.visits.authorization

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.authorization.composables.AuthScreen
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.rememberWindowSizeClass
import java.util.concurrent.Executor

@AndroidEntryPoint
class AuthActivity : FragmentActivity() {
    // Activity Cycle functions:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos la autenticación biométrica
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, authenticationCallback)

        setContent {
            val windowSizeClass = rememberWindowSizeClass()

            OmegaterapiaTheme {
                AuthScreen(windowSizeFormatClass = windowSizeClass)
            }
        }
    }


    //----------------------------------------------------------------------------------------------------------------
    // BIOMETRICS

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

