package das.omegaterapia.visits.activities.authorization.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.omegaterapia.visits.activities.authorization.AuthViewModel
import das.omegaterapia.visits.activities.authorization.BiometricAuthManager
import das.omegaterapia.visits.activities.authorization.DeviceBiometricsSupport
import das.omegaterapia.visits.ui.components.form.PasswordField
import das.omegaterapia.visits.ui.components.form.ValidatorOutlinedTextField
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.ui.theme.getButtonShape
import das.omegaterapia.visits.utils.canBeValidUsername
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginCard(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    biometricSupportChecker: () -> DeviceBiometricsSupport = { DeviceBiometricsSupport.UNSUPPORTED },
    onLoginSuccessful: (String) -> Unit = {},
    onSuccessfulBiometricLogin: () -> Unit = {},
) {
    Card(modifier = modifier, elevation = 8.dp) {
        LoginSection(
            authViewModel = authViewModel,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            biometricSupportChecker = biometricSupportChecker,
            onLoginSuccessful = onLoginSuccessful,
            onSuccessfulBiometricLogin = onSuccessfulBiometricLogin
        )
    }
}

@Composable
fun LoginSection(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    biometricSupportChecker: () -> DeviceBiometricsSupport = { DeviceBiometricsSupport.UNSUPPORTED },
    onLoginSuccessful: (String) -> Unit = {},
    onSuccessfulBiometricLogin: () -> Unit = {},
) {
    val context = LocalContext.current

    // States
    val coroutineScope = rememberCoroutineScope()
    var biometricSupport by rememberSaveable { mutableStateOf(biometricSupportChecker()) }

    var showLoginErrorDialog by rememberSaveable { mutableStateOf(false) }
    var showBiometricErrorDialogState by rememberSaveable { mutableStateOf(false) }
    var showBiometricEnrollDialogState by rememberSaveable { mutableStateOf(false) }

    // On login clicked action
    val onLogin: () -> Unit = {
        coroutineScope.launch(Dispatchers.IO) {
            val username = authViewModel.checkLogin()
            if (username != null) {
                onLoginSuccessful(username)
            } else showLoginErrorDialog = !authViewModel.isLoginCorrect
        }
    }

    val onBiometricAuthButtonClick: () -> Unit = {
        biometricSupport = biometricSupportChecker()
        when {
            authViewModel.lastLoggedUser == null -> showBiometricErrorDialogState = true
            biometricSupport == DeviceBiometricsSupport.NON_CONFIGURED -> showBiometricEnrollDialogState = true
            biometricSupport != DeviceBiometricsSupport.UNSUPPORTED -> onSuccessfulBiometricLogin()
        }
    }

    //--------------------------------------------------------------------------------------------------------------
    // DIALOGS
    if (showLoginErrorDialog) {
        AlertDialog(
            shape = RectangleShape,
            title = { Text(text = "Incorrect username or password.") },
            onDismissRequest = { showLoginErrorDialog = false },
            confirmButton = { TextButton(onClick = { showLoginErrorDialog = false }, shape = getButtonShape()) { Text(text = "OK") } }
        )
    }

    if (showBiometricErrorDialogState) {
        AlertDialog(
            shape = RectangleShape,
            title = { Text(text = "Invalid Account") },
            text = {
                Text(
                    text = "You haven't logged with a valid account before. Log with into an account with a valid password in order to enable biometrics.",
                )
            },
            onDismissRequest = { showBiometricErrorDialogState = false },
            confirmButton = {
                TextButton(onClick = { showBiometricErrorDialogState = false },
                    shape = getButtonShape()) { Text(text = "OK") }
            }
        )
    }

    if (showBiometricEnrollDialogState) {
        AlertDialog(
            shape = RectangleShape,
            title = { Text(text = "No biometrics enrolled") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.body1) {
                        Text(text = "Your device is capable of biometric login but none is enrolled.")
                        Text(text = "Do you want to configure them?")
                    }
                }
            },
            onDismissRequest = { showBiometricEnrollDialogState = false },
            confirmButton = {
                TextButton(
                    shape = getButtonShape(),
                    onClick = {
                        showBiometricEnrollDialogState = false
                        BiometricAuthManager.makeBiometricEnroll(context)
                    }
                ) { Text(text = "ENROLL") }
            },
            dismissButton = {
                TextButton(onClick = { showBiometricEnrollDialogState = false },
                    shape = getButtonShape()) { Text(text = "CANCEL") }
            }
        )
    }

    //--------------------------------------------------------------------------------------------------------------
    // MAIN UI
    CenteredColumn(
        modifier = modifier.width(IntrinsicSize.Min)
    ) {
        Text(text = "Login", style = MaterialTheme.typography.h5)

        Spacer(Modifier.height(8.dp))

        ValidatorOutlinedTextField(
            modifier = Modifier.widthIn(max = 280.dp),
            value = authViewModel.loginUsername,
            onValueChange = { if (canBeValidUsername(it)) authViewModel.loginUsername = it },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "person") },
            label = { Text(text = "Username") },
            isValid = authViewModel.isLoginCorrect,
            ignoreFirstTime = true
        )

        PasswordField(
            modifier = Modifier.widthIn(max = 280.dp),
            value = authViewModel.loginPassword,
            onValueChange = authViewModel::loginPassword::set,
            isValid = authViewModel.isLoginCorrect,
            ignoreFirstTime = true
        )

        Divider(Modifier.padding(top = 24.dp, bottom = 16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLogin,
            shape = getButtonShape(),
            enabled = authViewModel.loginUsername.isNotBlank() && authViewModel.loginPassword.isNotBlank()
        ) {
            Text(text = "Login")
        }

        Spacer(Modifier.height(8.dp))

        if (biometricSupport != DeviceBiometricsSupport.UNSUPPORTED) {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBiometricAuthButtonClick,
                shape = getButtonShape()
            ) {
                Icon(Icons.Filled.Fingerprint, contentDescription = "Biometric Authentication")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Biometric Authentication")
            }
        }
    }
}


//----------------------------------------------------------------------------------

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginCardPreview() {
    OmegaterapiaTheme {
        Surface {
            LoginCard(viewModel())
        }
    }
}
