package das.omegaterapia.visits.authorization.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.PasswordField
import das.omegaterapia.visits.ui.components.generic.ValidatorOutlinedTextField
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.canBeValidUsername
import das.omegaterapia.visits.authorization.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignInCard(authViewModel: AuthViewModel, modifier: Modifier = Modifier, onSignInSuccessful: (String) -> Unit = {}) {
    Card(modifier = modifier, elevation = 8.dp) {
        SignInSection(authViewModel, Modifier.padding(horizontal = 32.dp, vertical = 16.dp), onSignInSuccessful)
    }
}

@Composable
fun SignInSection(authViewModel: AuthViewModel, modifier: Modifier = Modifier, onSignInSuccessful: (String) -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val onSignIn: () -> Unit = {
        coroutineScope.launch(Dispatchers.IO) {
            val username = authViewModel.checkSignIn()
            if (username != null) {
                onSignInSuccessful(username)
            }
        }
    }

    CenteredColumn(
        modifier = modifier.width(IntrinsicSize.Min)
    ) {
        Text(text = "Sign In", style = MaterialTheme.typography.h5)

        Spacer(Modifier.height(8.dp))

        ValidatorOutlinedTextField(
            modifier = Modifier.widthIn(max = 280.dp),

            value = authViewModel.signInUsername,
            onValueChange = { if (canBeValidUsername(it)) authViewModel.signInUsername = it },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "person") },
            label = { Text(text = "Username") },
            isValid = authViewModel.isSignInUsernameValid && !authViewModel.signInUserExists
        )

        PasswordField(
            modifier = Modifier.widthIn(max = 280.dp),

            value = authViewModel.signInPassword,
            onValueChange = authViewModel::signInPassword::set,
            isValid = authViewModel.isSignInPasswordValid
        )

        PasswordField(
            modifier = Modifier.widthIn(max = 280.dp),

            value = authViewModel.signInConfirmationPassword,
            onValueChange = authViewModel::signInConfirmationPassword::set,
            isValid = authViewModel.isSignInPasswordConfirmationValid,

            label = { Text(text = "Confirm Password") },
            placeholder = { Text(text = "Confirm Password") },
        )

        Divider(Modifier.padding(top = 24.dp, bottom = 16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSignIn
        ) {
            Text(text = "Sign In")
        }
    }
}

//----------------------------------------------------------------------------------

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignInCardPreview() {
    OmegaterapiaTheme {
        Surface() {
            SignInCard(viewModel())
        }
    }
}