package das.omegaterapia.visits.activities.main.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import das.omegaterapia.visits.R
import das.omegaterapia.visits.activities.main.VisitsViewModel
import das.omegaterapia.visits.ui.components.form.FormSubsection
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.navigation.BackArrowTopBar
import das.omegaterapia.visits.utils.LanguagePickerDialog
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserProfileScreen(
    title: String,
    visitsViewModel: VisitsViewModel,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {},
) {
    val context = LocalContext.current

    Scaffold(topBar = { BackArrowTopBar(title, onBackPressed) }) {
        CenteredColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
        ) {

            var showSelectLangDialog by rememberSaveable { mutableStateOf(false) }
            val userSelectedLanguage by preferencesViewModel.currentPrefLang.collectAsState(initial = preferencesViewModel.currentSetLang)

            //------------------------------------------------------------------------------------
            // DIALOGS
            //------------------------------------------------------------------------------------
            if (showSelectLangDialog) {
                LanguagePickerDialog(
                    title = "Select Language",
                    selectedLanguage = userSelectedLanguage,
                    onLanguageSelected = { preferencesViewModel.changeLang(it, context); showSelectLangDialog = false },
                    onDismiss = { showSelectLangDialog = false }
                )
            }


            //------------------------------------------------------------------------------------
            // UI
            //------------------------------------------------------------------------------------


            // Title

            Icon(
                modifier = Modifier.size(160.dp),
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(preferencesViewModel.currentUser, style = MaterialTheme.typography.h6)

            Divider(Modifier.padding(top = 40.dp, bottom = 16.dp))


            ListItem(
                icon = { Icon(Icons.Filled.Language, null, Modifier.padding(top = 7.dp)) },
                secondaryText = { Text(text = userSelectedLanguage.language) },
                modifier = Modifier.clickable { showSelectLangDialog = true }
            ) {
                Text(text = "Application Language")
            }

            FormSubsection(title = "Visits Screen Section's Date Format", Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp))

            ListItem(
                icon = { Icon(Icons.Filled.Language, null, Modifier.padding(top = 7.dp)) },
                secondaryText = { Text(text = userSelectedLanguage.language) },
                modifier = Modifier.clickable { showSelectLangDialog = true }
            ) {
                Text(text = "Today's Visits' Date Format")
            }

            ListItem(
                icon = { Icon(Icons.Filled.Language, null, Modifier.padding(top = 7.dp)) },
                secondaryText = { Text(text = userSelectedLanguage.language) },
                modifier = Modifier.clickable { showSelectLangDialog = true }
            ) {
                Text(text = "All and Vip Visits' Section Date Format")
            }



            Divider(Modifier.padding(top = 16.dp, bottom = 16.dp))

            SaveAsJSONSection(visitsViewModel)
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SaveAsJSONSection(visitsViewModel: VisitsViewModel) {
    val contentResolver = LocalContext.current.contentResolver
    val saverLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument()) { uri ->
        if (uri != null) {
            try {
                contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                        fileOutputStream.write(
                            (visitsViewModel.todaysVisitsJson()).toByteArray()
                        )
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    val filename = "visits_${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}.json"
    val action = { saverLauncher.launch(filename) }

    ListItem(
        Modifier.clickable(onClick = action),
        icon = { Icon(Icons.Filled.Save, "Save today's visits", Modifier.padding(top = 7.dp)) },
        secondaryText = { Text(text = filename) }
    ) {
        Text(text = stringResource(R.string.save_json_section_title), style = MaterialTheme.typography.subtitle1)
    }
}

