package das.omegaterapia.visits

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import das.omegaterapia.visits.ui.screens.add.VisitForm
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {

    // Activity Cycle functions:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos la autenticación biométrica
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, authenticationCallback)

        setContent {
            OmegaterapiaTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    VisitForm({})
                }
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

