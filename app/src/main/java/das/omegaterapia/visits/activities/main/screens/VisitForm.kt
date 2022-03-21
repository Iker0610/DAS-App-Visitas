package das.omegaterapia.visits.activities.main.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.MapsHomeWork
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.model.entities.Client
import das.omegaterapia.visits.model.entities.Direction
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitData
import das.omegaterapia.visits.ui.components.datetime.AlternativeOutlinedDateTimeField
import das.omegaterapia.visits.ui.components.form.FormSection
import das.omegaterapia.visits.ui.components.form.FormSubsection
import das.omegaterapia.visits.ui.components.form.ValidatorOutlinedTextField
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.components.generic.OutlinedChoiceChip
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.ui.theme.getMaterialRectangleShape
import das.omegaterapia.visits.utils.canBePhoneNumber
import das.omegaterapia.visits.utils.canBeZIP
import das.omegaterapia.visits.utils.formatPhoneNumber
import das.omegaterapia.visits.utils.isNonEmptyText
import das.omegaterapia.visits.utils.isText
import das.omegaterapia.visits.utils.isValidPhoneNumber
import das.omegaterapia.visits.utils.isZIP
import das.omegaterapia.visits.utils.rememberMutableStateListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VisitForm(
    submitForm: suspend (VisitCard) -> Boolean,
    modifier: Modifier = Modifier,
    onSuccessfulSubmit: () -> Unit = {},
    initialVisitCard: VisitCard? = null,
) {
    /*
    * TODO:
    * - Añadir on IME action
    * - Mejorar el botón VIP y el de calendario / hora
    */

    // Courutine Scope
    val scope = rememberCoroutineScope()
    var showErrorDialog by rememberSaveable { mutableStateOf(false) }

    // Variables para guardar los datos
    val (isVIP, setIsVIP) = rememberSaveable { mutableStateOf(initialVisitCard?.visitData?.isVIP ?: false) }
    val (visitDate, setVisitDate) = rememberSaveable { mutableStateOf(initialVisitCard?.visitData?.visitDate ?: ZonedDateTime.now()) }

    val (clientNameText, setClientNameText) = rememberSaveable { mutableStateOf(initialVisitCard?.client?.name ?: "") }
    val (clientSurnameText, setClientSurnameText) = rememberSaveable { mutableStateOf(initialVisitCard?.client?.surname ?: "") }
    val clientCompanions = rememberMutableStateListOf(*(initialVisitCard?.visitData?.companions?.toTypedArray() ?: arrayOf("")))

    val (addressText, setAddressText) = rememberSaveable { mutableStateOf(initialVisitCard?.client?.direction?.address ?: "") }
    val (townText, setTownText) = rememberSaveable { mutableStateOf(initialVisitCard?.client?.direction?.town ?: "") }
    val (zipCodeText, setZIPCodeText) = rememberSaveable { mutableStateOf(initialVisitCard?.client?.direction?.zip ?: "") }
    val (phoneText, setPhoneText) = rememberSaveable { mutableStateOf(initialVisitCard?.client?.phoneNum ?: "") }

    val (observationText, setObservationText) = rememberSaveable { mutableStateOf(initialVisitCard?.visitData?.observations ?: "") }


    // Variable para comprobación de datos
    val isNameValid by remember(clientNameText) { derivedStateOf { isNonEmptyText(clientNameText) } }
    val isSurnameValid by remember(clientSurnameText) { derivedStateOf { isNonEmptyText(clientSurnameText) } }

    val isAddressValid by remember(addressText) { derivedStateOf { addressText.isNotBlank() } }
    val isTownValid by remember(townText) { derivedStateOf { townText.isNotBlank() } }
    val isZIPValid by remember(zipCodeText) { derivedStateOf { isZIP(zipCodeText) } }
    val isPhoneValid by remember(phoneText) { derivedStateOf { isValidPhoneNumber(phoneText) } }

    val areAllValid = isNameValid && isSurnameValid && isAddressValid && isTownValid && isZIPValid && isPhoneValid


    // Dialogs
    if (showErrorDialog) {
        if (initialVisitCard == null) {
            AlertDialog(
                shape = getMaterialRectangleShape(),
                title = { Text(text = "Couldn't add a new Visit Card") },
                text = { Text(text = "An error occurred when adding the Visit Card. Try again.") },
                onDismissRequest = { showErrorDialog = false },
                confirmButton = { TextButton(onClick = { showErrorDialog = false }, shape = getMaterialRectangleShape()) { Text(text = "DISMISS") } }
            )
        } else {
            AlertDialog(
                shape = getMaterialRectangleShape(),
                title = { Text(text = "Couldn't edit the Visit Card") },
                text = { Text(text = "An error occurred when editing the Visit Card. Try again.") },
                onDismissRequest = { showErrorDialog = false },
                confirmButton = { TextButton(onClick = { showErrorDialog = false }, shape = getMaterialRectangleShape()) { Text(text = "DISMISS") } }
            )
        }
    }


    // UI
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        FormSection(title = "Visit Data") {
            CenteredRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val iconSize = 28.dp
                OutlinedChoiceChip(
                    modifier = Modifier.height(IntrinsicSize.Max).padding(top = 8.dp),
                    onClick = { setIsVIP(!isVIP) },
                    selected = isVIP,
                    leadingIcon = {
                        if (isVIP) {
                            Icon(Icons.Filled.Star, contentDescription = "VIP Client",
                                Modifier
                                    .size(iconSize)
                                    .padding(start = 8.dp))
                        } else {
                            Icon(
                                Icons.Filled.StarOutline, contentDescription = "Not VIP Client",
                                Modifier
                                    .size(iconSize)
                                    .padding(start = 8.dp)
                            )
                        }
                    },
                ) {
                    Text(text = "VIP", style = MaterialTheme.typography.body1, modifier = Modifier.padding(end = 8.dp))
                }

                AlternativeOutlinedDateTimeField(
                    date = visitDate,
                    onDateTimeSelected = setVisitDate,
                    requireFutureDateTime = initialVisitCard == null,

                    dateLabel = { Text(text = "Date*") },
                    timeLabel = { Text(text = "Time*") },

                    dialogTitle = "Select your visit's Date and Hour"
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
                                Icon(Icons.Filled.RemoveCircle, contentDescription = "Delete Companion", tint = MaterialTheme.colors.secondary)
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
            shape = getMaterialRectangleShape(),
            onClick = {
                val clientData = Client(
                    name = clientNameText,
                    surname = clientSurnameText,
                    phoneNum = phoneText,
                    direction = Direction(addressText, townText, zipCodeText)
                )

                val visitData: VisitData

                if (initialVisitCard != null) {
                    visitData = initialVisitCard.visitData.copy(
                        mainClientPhone = clientData.phoneNum,
                        companions = clientCompanions,
                        visitDate = visitDate,
                        isVIP = isVIP,
                        observations = observationText
                    )
                } else {
                    visitData = VisitData(
                        mainClientPhone = clientData.phoneNum,
                        companions = clientCompanions,
                        visitDate = visitDate,
                        isVIP = isVIP,
                        observations = observationText
                    )
                }
                scope.launch(Dispatchers.IO) {
                    val operationOK = submitForm(VisitCard(visitData, clientData))

                    if (operationOK) onSuccessfulSubmit()
                    else showErrorDialog = true
                }
            },
            enabled = areAllValid
        ) {
            Text(text = "Add new Visit Card")
        }
    }
}


//-----------------------------------------------------------------------------------------

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddVisitCardScreenPreview() {
    OmegaterapiaTheme {
        Surface {
            VisitForm({ true })
        }
    }
}