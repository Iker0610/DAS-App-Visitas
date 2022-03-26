package das.omegaterapia.visits.activities.main.composables.form

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import das.omegaterapia.visits.R
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.ui.components.datetime.AlternativeOutlinedDateTimeField
import das.omegaterapia.visits.ui.components.form.FormSection
import das.omegaterapia.visits.ui.components.form.FormSubsection
import das.omegaterapia.visits.ui.components.form.ValidatorOutlinedTextField
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.components.generic.FixedOutlinedButton
import das.omegaterapia.visits.ui.components.generic.OutlinedChoiceChip
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import das.omegaterapia.visits.ui.theme.getButtonShape
import das.omegaterapia.visits.utils.canBePhoneNumber
import das.omegaterapia.visits.utils.canBeZIP
import das.omegaterapia.visits.utils.formatPhoneNumber
import das.omegaterapia.visits.utils.isText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VisitForm(
    submitVisitCard: suspend (VisitCard) -> Boolean,
    modifier: Modifier = Modifier,
    onSuccessfulSubmit: () -> Unit = {},
    initialVisitCard: VisitCard? = null,
) {

    // TODO: Añadir on IME action

    val visitFormViewModel: VisitFormViewModel = hiltViewModel()

    val editMode = initialVisitCard != null

    LaunchedEffect(true) {
        if (initialVisitCard != null) visitFormViewModel.initializeWithVisitCard(initialVisitCard)
    }


    // Courutine Scope
    val scope = rememberCoroutineScope()

    // Dialog State
    var showErrorDialog by rememberSaveable { mutableStateOf(false) }

    // Dialogs
    if (showErrorDialog) {
        if (editMode) {
            AlertDialog(
                shape = RectangleShape,
                title = { Text(text = stringResource(R.string.visit_card_edit_failed_dialog_title)) },
                text = { Text(text = stringResource(R.string.visit_card_edit_failed_dialog_text)) },
                onDismissRequest = { showErrorDialog = false },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false },
                        shape = getButtonShape()) { Text(text = stringResource(R.string.dismiss_button)) }
                }
            )
        } else {
            AlertDialog(
                shape = RectangleShape,
                title = { Text(text = stringResource(R.string.visit_card_creation_failed_dialog_title)) },
                text = { Text(text = stringResource(R.string.visit_card_creation_failed_dialog_text)) },
                onDismissRequest = { showErrorDialog = false },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false },
                        shape = getButtonShape()) { Text(text = stringResource(R.string.dismiss_button)) }
                }
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

        FormSection(title = stringResource(R.string.visit_card_visit_data_section_title)) {
            CenteredRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val iconSize = 28.dp
                OutlinedChoiceChip(
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .padding(top = 8.dp),
                    onClick = { visitFormViewModel.isVIP = !visitFormViewModel.isVIP },
                    selected = visitFormViewModel.isVIP,
                    leadingIcon = {
                        if (visitFormViewModel.isVIP) {
                            Icon(Icons.Filled.Star, null,
                                Modifier
                                    .size(iconSize)
                                    .padding(start = 8.dp))
                        } else {
                            Icon(
                                Icons.Filled.StarOutline, null,
                                Modifier
                                    .size(iconSize)
                                    .padding(start = 8.dp)
                            )
                        }
                    },
                ) {
                    Text(text = stringResource(R.string.vip_chip), style = MaterialTheme.typography.body1, modifier = Modifier.padding(end = 8.dp))
                }

                AlternativeOutlinedDateTimeField(
                    date = visitFormViewModel.visitDate,
                    onDateTimeSelected = visitFormViewModel::visitDate::set,
                    requireFutureDateTime = !editMode,

                    dateLabel = { Text(text = "${stringResource(R.string.visit_card_date_label)}*") },
                    timeLabel = { Text(text = "${stringResource(R.string.visit_card_time_label)}*") },
                )
            }
        }

        FormSection(title = stringResource(R.string.visit_card_clients_section_title)) {
            FormSubsection(title = stringResource(R.string.visit_card_main_client)) {

                ValidatorOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),

                    label = { Text(text = "${stringResource(R.string.visit_card_client_name_label)}*") },
                    leadingIcon = { Icon(Icons.Default.Person, stringResource(R.string.visit_card_client_name_label)) },

                    value = visitFormViewModel.clientNameText,
                    onValueChange = { if (isText(it)) visitFormViewModel.clientNameText = it },
                    isValid = visitFormViewModel.isNameValid,
                    ignoreFirstTime = editMode,

                    singleLine = true,
                    maxLines = 1
                )

                ValidatorOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),

                    label = { Text(text = "${stringResource(R.string.visit_card_client_surname_label)}*") },

                    value = visitFormViewModel.clientSurnameText,
                    onValueChange = { if (isText(it)) visitFormViewModel.clientSurnameText = it },
                    isValid = visitFormViewModel.isSurnameValid,
                    ignoreFirstTime = editMode,

                    singleLine = true,
                    maxLines = 1
                )
            }

            FormSubsection(title = stringResource(R.string.visit_card_companions)) {
                visitFormViewModel.clientCompanions.forEachIndexed { index, companion ->
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),

                        label = { Text(text = stringResource(R.string.visit_card_companion_name_label)) },
                        leadingIcon = { Icon(Icons.Default.People, stringResource(R.string.visit_card_companion_name_label)) },
                        trailingIcon = {
                            IconButton(onClick = {
                                visitFormViewModel.clientCompanions.removeAt(index)
                                if (visitFormViewModel.clientCompanions.isEmpty()) visitFormViewModel.clientCompanions.add("")
                            }
                            ) {
                                Icon(Icons.Filled.RemoveCircle,
                                    stringResource(R.string.remove_companion_button),
                                    tint = MaterialTheme.colors.secondary)
                            }
                        },

                        value = companion,
                        onValueChange = { if (isText(it)) visitFormViewModel.clientCompanions[index] = it },

                        singleLine = true,
                        maxLines = 1
                    )
                }
                FixedOutlinedButton(onClick = { visitFormViewModel.clientCompanions.add("") }, Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = stringResource(R.string.add_companion_button))
                }
            }
        }

        FormSection(title = stringResource(R.string.visit_card_location_section_title)) {

            ValidatorOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),

                label = { Text(text = "${stringResource(R.string.visit_card_phone_label)}*") },
                leadingIcon = { Icon(Icons.Default.ContactPhone, null) },

                value = visitFormViewModel.phoneText,
                onValueChange = { if (canBePhoneNumber(it)) visitFormViewModel.phoneText = formatPhoneNumber(it) },
                isValid = visitFormViewModel.isPhoneValid,
                ignoreFirstTime = editMode,

                singleLine = true,
                maxLines = 1
            )

            ValidatorOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),

                label = { Text(text = "${stringResource(R.string.visit_card_address_label)}*") },
                leadingIcon = { Icon(Icons.Default.MapsHomeWork, stringResource(R.string.visit_card_address_label)) },

                value = visitFormViewModel.addressText,
                onValueChange = visitFormViewModel::addressText::set,
                isValid = visitFormViewModel.isAddressValid,
                ignoreFirstTime = editMode,

                singleLine = true,
                maxLines = 1
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                ValidatorOutlinedTextField(
                    modifier = Modifier.weight(2f),

                    label = { Text(text = "${stringResource(R.string.visit_card_town_label)}*") },
                    leadingIcon = { Icon(Icons.Default.PinDrop, stringResource(R.string.visit_card_town_label)) },

                    value = visitFormViewModel.townText,
                    onValueChange = { if (isText(it)) visitFormViewModel.townText = it },
                    isValid = visitFormViewModel.isTownValid,
                    ignoreFirstTime = editMode,

                    singleLine = true,
                    maxLines = 1
                )

                ValidatorOutlinedTextField(
                    modifier = Modifier.weight(1f),

                    label = { Text(text = "${stringResource(R.string.visit_card_zip_label)}*") },
                    leadingIcon = {
                        Icon(Icons.Default.MyLocation,
                            contentDescription = stringResource(R.string.visit_card_zip_label_icon_description))
                    },

                    value = visitFormViewModel.zipCodeText,
                    onValueChange = { if (canBeZIP(it)) visitFormViewModel.zipCodeText = it },
                    isValid = visitFormViewModel.isZIPValid,
                    ignoreFirstTime = editMode,

                    singleLine = true,
                    maxLines = 1
                )
            }
        }

        FormSection(title = stringResource(R.string.visit_card_observations)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                label = { Text(text = stringResource(R.string.visit_card_observations)) },
                leadingIcon = { Icon(Icons.Default.Visibility, contentDescription = stringResource(R.string.visit_card_observations)) },

                value = visitFormViewModel.observationText,
                onValueChange = visitFormViewModel::observationText::set
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = getButtonShape(),
            onClick = {
                scope.launch(Dispatchers.IO) {
                    val operationOK = submitVisitCard(visitFormViewModel.generateVisitCard())

                    if (operationOK) onSuccessfulSubmit()
                    else showErrorDialog = true
                }
            },
            enabled = visitFormViewModel.areAllValid
        ) {
            Text(text = if (initialVisitCard == null) stringResource(R.string.add_visit_card_button) else stringResource(R.string.edit_visit_card_button))
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