package das.omegaterapia.visits.ui.screens.add

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.model.VisitCard
import das.omegaterapia.visits.ui.components.FormSection
import das.omegaterapia.visits.ui.components.FormSubsection
import das.omegaterapia.visits.ui.components.generic.OutlinedChoiceChip
import das.omegaterapia.visits.ui.components.generic.ValidatorOutlinedTextField
import das.omegaterapia.visits.ui.components.generic.rememberMutableStateListOf
import das.omegaterapia.visits.ui.components.selectDateTime
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.utils.*
import java.time.LocalDateTime


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VisitForm(
    onSubmitForm: (VisitCard) -> Unit,
    modifier: Modifier = Modifier,
    initialVisitCard: VisitCard? = null,
) {

    // Variables para guardar los datos
    val (isVIP, setIsVIP) = rememberSaveable { mutableStateOf(initialVisitCard?.isVIP ?: false) }

    val (clientNameText, setClientNameText) = rememberSaveable { mutableStateOf(initialVisitCard?.mainClient?.name ?: "") }
    val (clientSurnameText, setClientSurnameText) = rememberSaveable { mutableStateOf(initialVisitCard?.mainClient?.surname ?: "") }

    val clientCompanions = rememberMutableStateListOf(*(initialVisitCard?.companions?.toTypedArray() ?: arrayOf("")))


    val (addressText, setAddressText) = rememberSaveable { mutableStateOf(initialVisitCard?.mainClient?.direction?.address ?: "") }
    val (townText, setTownText) = rememberSaveable { mutableStateOf(initialVisitCard?.mainClient?.direction?.town ?: "") }
    val (zipCodeText, setZIPCodeText) = rememberSaveable { mutableStateOf(initialVisitCard?.mainClient?.direction?.zip ?: "") }

    val (phoneText, setPhoneText) = rememberSaveable { mutableStateOf(initialVisitCard?.mainClient?.phoneNum ?: "") }

    // TODO - Formatear la fecha
    val (dateText, setDateText) = rememberSaveable { mutableStateOf(initialVisitCard?.visitDate ?: LocalDateTime.now()) }

    val (observationText, setObservationText) = rememberSaveable { mutableStateOf(initialVisitCard?.observations ?: "") }


    // Variable para comprobación de datos
    val isNameValid by remember(clientNameText) { derivedStateOf { isNonEmptyText(clientNameText) } }
    val isSurnameValid by remember(clientSurnameText) { derivedStateOf { isNonEmptyText(clientSurnameText) } }

    val isAddressValid by remember(addressText) { derivedStateOf { addressText.isNotBlank() } }
    val isTownValid by remember(townText) { derivedStateOf { townText.isNotBlank() } }
    val isZIPValid by remember(zipCodeText) { derivedStateOf { isZIP(zipCodeText) } }

    val isPhoneValid by remember(phoneText) { derivedStateOf { isValidPhoneNumber(phoneText) } }

    val areAllValid = isNameValid && isSurnameValid && isAddressValid && isTownValid && isZIPValid && isPhoneValid

    val context = LocalContext.current
    // UI
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FormSection(
            title = "Visit Data",
            trailingContent = {
                val iconSize = 20.dp
                OutlinedChoiceChip(
                    onClick = { setIsVIP(!isVIP) },
                    selected = isVIP,
                    leadingIcon = {
                        if (isVIP) {
                            Icon(Icons.Filled.Star, contentDescription = "VIP Client", Modifier.size(iconSize))
                        } else {
                            Icon(Icons.Filled.StarOutline, contentDescription = "Not VIP Client", Modifier.size(iconSize))
                        }
                    },
                ) {
                    Text(text = "VIP", style = MaterialTheme.typography.body2)
                }
            }) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    modifier = Modifier.weight(2f),

                    label = { Text(text = "Date*") },
                    leadingIcon = {
                        IconButton(onClick = {
                            selectDateTime(context) {
                                Toast
                                    .makeText(context, it.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) {
                            Icon(Icons.Default.Event, contentDescription = "Visit Date")
                        }
                    },
                    placeholder = { Text(text = "dd/mm/yyyy") },

                    value = dateText.toString(),
                    onValueChange = {},

                    readOnly = true,

                    singleLine = true,
                    maxLines = 1
                )
            }
        }

        FormSection(title = "Clients") {
            FormSubsection(title = "Main Client") {

                ValidatorOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),

                    label = { Text(text = "Client Name*") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Client name and surname") },

                    value = clientNameText,
                    onValueChange = { if (isText(it)) setClientNameText(it) },
                    isValid = isNameValid,

                    singleLine = true,
                    maxLines = 1
                )

                ValidatorOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),

                    label = { Text(text = "Client Surname*") },

                    value = clientSurnameText,
                    onValueChange = { if (isText(it)) setClientSurnameText(it) },
                    isValid = isSurnameValid,

                    singleLine = true,
                    maxLines = 1
                )
            }

            FormSubsection(title = "Client's Companions") {
                clientCompanions.forEachIndexed { index, companion ->
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),

                        label = { Text(text = "Name and Surname") },
                        leadingIcon = { Icon(Icons.Default.People, contentDescription = "Client's companion name and surname") },
                        trailingIcon = {
                            IconButton(onClick = { clientCompanions.removeAt(index) }) {
                                Icon(Icons.Filled.RemoveCircle, contentDescription = "Delete Companion")
                            }
                        },

                        value = companion,
                        onValueChange = { if (isText(it)) clientCompanions[index] = it },

                        singleLine = true,
                        maxLines = 1
                    )
                }
                OutlinedButton(onClick = { clientCompanions.add("") }, Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Add Companion")
                }
            }
        }

        FormSection(title = "Location Data") {

            ValidatorOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),

                label = { Text(text = "Phone Number*") },
                leadingIcon = { Icon(Icons.Default.ContactPhone, contentDescription = "Phone Number") },

                value = phoneText,
                onValueChange = { if (canBePhoneNumber(it)) setPhoneText(formatPhoneNumber(it)) },
                isValid = isPhoneValid,

                singleLine = true,
                maxLines = 1
            )

            ValidatorOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),

                label = { Text(text = "Address*") },
                leadingIcon = { Icon(Icons.Default.MapsHomeWork, contentDescription = "Address") },

                value = addressText,
                onValueChange = setAddressText,
                isValid = isAddressValid,

                singleLine = true,
                maxLines = 1
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                ValidatorOutlinedTextField(
                    modifier = Modifier.weight(2f),

                    label = { Text(text = "Town*") },
                    leadingIcon = { Icon(Icons.Default.PinDrop, contentDescription = "Town") },

                    value = townText,
                    onValueChange = { if (isText(it)) setTownText(it) },
                    isValid = isTownValid,

                    singleLine = true,
                    maxLines = 1
                )

                ValidatorOutlinedTextField(
                    modifier = Modifier.weight(1f),

                    label = { Text(text = "ZIP*") },
                    leadingIcon = { Icon(Icons.Default.MyLocation, contentDescription = "ZIP Code") },

                    value = zipCodeText,
                    onValueChange = { if (canBeZIP(it)) setZIPCodeText(it) },
                    isValid = isZIPValid,

                    singleLine = true,
                    maxLines = 1
                )
            }
        }

        FormSection(title = "Observations") {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                label = { Text(text = "Observations") },
                leadingIcon = { Icon(Icons.Default.Visibility, contentDescription = "Observations") },

                value = observationText,
                onValueChange = setObservationText
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ },
            enabled = areAllValid
        ) {
            Text(text = "Add new Visit Card")
        }
    }
}


//-----------------------------------------------------------------------------------------

@Preview()
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddVisitCardScreenPreview() {
    OmegaterapiaTheme {
        Surface {
            VisitForm({})
        }
    }
}