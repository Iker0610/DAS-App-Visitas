package das.omegaterapia.visits.ui.components.generic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: @Composable (() -> Unit)? = { Text("Password") },
    placeholder: @Composable (() -> Unit)? = { Text("Password") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Filled.VpnKey, contentDescription = "Password") },
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        label = label,
        placeholder = placeholder,

        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        leadingIcon = leadingIcon,
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(imageVector = image, description) }
        }
    )
}

@Composable
fun ValidatorOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,

    modifier: Modifier = Modifier,

    isValid: Boolean = true,
    ignoreFirstTime: Boolean = false,

    label: @Composable () -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,

    singleLine: Boolean = true,
    maxLines: Int = 1,

    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    var isFirstTime: Boolean by rememberSaveable { mutableStateOf(true) }
    if (ignoreFirstTime) isFirstTime = false

    OutlinedTextField(
        value = value,
        onValueChange = { isFirstTime = false; onValueChange(it) },

        isError = !(isFirstTime || isValid),

        modifier = modifier,

        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,

        singleLine = singleLine,
        maxLines = maxLines,

        enabled = enabled,
        readOnly = readOnly,
    )
}

@Composable
fun TextIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    icon: ImageVector,
    text: String,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        Icon(icon, "Search in Google Maps", Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(2.dp))
        Text(text = text)
    }
}