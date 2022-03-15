package das.omegaterapia.visits.ui.screens.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
fun LogInScreen() {
    Scaffold(
        topBar = { TopAppBar { Text(text = "Login") } }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            LogInCard()
        }
    }

}

@Composable
fun LogInCard(modifier: Modifier = Modifier) {
    Card {
        LogInSection(Modifier.padding(20.dp))
    }
}

@Composable
fun LogInSection(modifier: Modifier = Modifier) {
    val (username, setUsername) = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    val (password, setPassword) = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }

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

@Composable
fun SignInCard(modifier: Modifier = Modifier) {
    Card {
        SignInSection(Modifier.padding(20.dp))
    }
}

@Composable
fun SignInSection(modifier: Modifier = Modifier) {
    val (username, setUsername) = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    val (password, setPassword) = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(IntrinsicSize.Min)
    ) {
        Text(text = "Sign In", style = MaterialTheme.typography.h5)

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

        PasswordField(
            value = password,
            onValueChange = setPassword,
            label = { Text(text = "Confirm Password") },
            placeholder = { Text(text = "Confirm Password") }
        )

        Divider(Modifier.padding(vertical = 16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Sign In")
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

//----------------------------------------------------------------------------------------------------------

@Preview(
    name = "LoginPreview-Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    OmegaterapiaTheme {
        LogInScreen()
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(showBackground = true)
@Composable
fun LoginCardPreview(){
    LogInCard()
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(showBackground = true)
@Composable
fun SignInCardPreview(){
    SignInCard()
}
