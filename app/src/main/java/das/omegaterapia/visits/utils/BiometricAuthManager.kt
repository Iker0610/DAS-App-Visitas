package das.omegaterapia.visits.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

enum class DeviceBiometricsSupport {
    SUPPORTED,
    NON_CONFIGURED,
    UNSUPPORTED
}

class BiometricAuthManager(
    private val context: Context, private val authUser: String, onAuthenticationSucceeded: (String) -> Unit,
) {
    private val biometricManager = BiometricManager.from(context)


    private val authenticationCallback: BiometricPrompt.AuthenticationCallback =
        // Con "object" definimos un singleton que hereda de BiometricPrompt.AuthenticationCallback
        object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthenticationSucceeded(authUser)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Authentication Failed. Try again.", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == BiometricPrompt.ERROR_LOCKOUT) {
                    Toast.makeText(context, "$errString", Toast.LENGTH_SHORT).show()
                } else if (errorCode != BiometricPrompt.ERROR_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON && errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                    Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }
            }
        }


    private val biometricPromptInfo: BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login as $authUser")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()


    private val biometricPrompt: BiometricPrompt =
        BiometricPrompt(context as FragmentActivity, ContextCompat.getMainExecutor(context), authenticationCallback)


    fun submitBiometricAuthorization() {
        biometricPrompt.authenticate(biometricPromptInfo)
    }


    fun checkBiometricSupport(): DeviceBiometricsSupport {
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> DeviceBiometricsSupport.SUPPORTED
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> DeviceBiometricsSupport.NON_CONFIGURED
            else -> DeviceBiometricsSupport.UNSUPPORTED
        }
    }

    companion object {
        fun makeBiometricEnroll(context: Context) {
            val intent: Intent = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    Intent(Settings.ACTION_BIOMETRIC_ENROLL).putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG)
                }
                Build.VERSION.SDK_INT == Build.VERSION_CODES.P -> {
                    @Suppress("DEPRECATION")
                    Intent(Settings.ACTION_FINGERPRINT_ENROLL)
                }
                else -> {
                    Intent(Settings.ACTION_SECURITY_SETTINGS)
                }
            }
            try {
                context.startActivity(intent)
            } catch (error: ActivityNotFoundException) {
                context.startActivity(Intent(Settings.ACTION_SETTINGS))
            }
        }
    }
}


