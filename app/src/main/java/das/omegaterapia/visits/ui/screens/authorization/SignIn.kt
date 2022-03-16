package das.omegaterapia.visits.ui.screens.authorization

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.PasswordField
import das.omegaterapia.visits.ui.components.generic.ValidatorOutlinedTextField
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.isValidPassword
import das.omegaterapia.visits.utils.isValidUsername
import das.omegaterapia.visits.viewmodel.AuthViewModel

@Composable
fun SignInCard(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    Card(modifier = modifier, elevation = 8.dp) {
        SignInSection(authViewModel, Modifier.padding(horizontal = 32.dp, vertical = 16.dp))
    }
}

@Composable
fun SignInSection(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {

    CenteredColumn(
        modifier = modifier.width(IntrinsicSize.Min)
    ) {
        Text(text = "Sign In", style = MaterialTheme.typography.h5)

        Spacer(Modifier.height(8.dp))

        ValidatorOutlinedTextField(
            modifier = Modifier.widthIn(max = 280.dp),

            value = authViewModel.signInUsername,
            onValueChange = { if (isValidUsername(it)) authViewModel.signInUsername = it },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "person") },
            label = { Text(text = "Username") }
        )

        PasswordField(
            modifier = Modifier.widthIn(max = 280.dp),

            value = authViewModel.signInPassword,
            onValueChange = authViewModel::signInPassword::set,

            isValid = isValidPassword(authViewModel.signInPassword)
        )

        PasswordField(
            modifier = Modifier.widthIn(max = 280.dp),

            value = authViewModel.signInConfirmationPassword,
            onValueChange = authViewModel::signInConfirmationPassword::set,
            label = { Text(text = "Confirm Password") },
            placeholder = { Text(text = "Confirm Password") },

            isValid = isValidPassword(authViewModel.signInConfirmationPassword) && authViewModel.signInPassword == authViewModel.signInConfirmationPassword
        )

        Divider(Modifier.padding(top = 24.dp, bottom = 16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = authViewModel::submitSignIn
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
            SignInCard(AuthViewModel())
        }
    }
}