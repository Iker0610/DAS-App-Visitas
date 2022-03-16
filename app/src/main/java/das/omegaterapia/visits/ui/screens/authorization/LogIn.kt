package das.omegaterapia.visits.ui.screens.authorization

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.ui.components.generic.PasswordField
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme

@Composable
fun LogInCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        LogInSection(Modifier.padding(16.dp))
    }
}

@Composable
fun LogInSection(modifier: Modifier = Modifier) {
    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(IntrinsicSize.Min)
    ) {
        Text(text = "Log In", style = MaterialTheme.typography.h5)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "person") },
            label = { Text(text = "Username") }
        )

        PasswordField(
            value = password,
            onValueChange = setPassword,
        )

        Divider(Modifier.padding(vertical = 16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Login")
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* TODO */ }
        ) {

            Icon(
                Icons.Filled.Fingerprint,
                contentDescription = "Biometric Authentication")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Biometric Authentication")

        }
    }
}

//----------------------------------------------------------------------------------

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginCardPreview() {
    OmegaterapiaTheme {
        Surface() {
            LogInCard()
        }
    }
}
