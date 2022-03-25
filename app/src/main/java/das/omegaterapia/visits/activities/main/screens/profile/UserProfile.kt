package das.omegaterapia.visits.activities.main.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import das.omegaterapia.visits.R
import das.omegaterapia.visits.activities.main.VisitsViewModel
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.navigation.BackArrowTopBar
import das.omegaterapia.visits.utils.LanguagePickerDialog
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun UserProfileScreen(
    title: String,
    visitsViewModel: VisitsViewModel,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {},
) {
    Scaffold(topBar = { BackArrowTopBar(title, onBackPressed) }) {
        CenteredColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            var showSelectLangDialog by rememberSaveable { mutableStateOf(false) }
            val userSelectedLanguage = preferencesViewModel.currentPrefLang.collectAsState(initial = preferencesViewModel.currentSetLang).value

            //------------------------------------------------------------------------------------
            // DIALOGS
            //------------------------------------------------------------------------------------
            if (showSelectLangDialog) {
                LanguagePickerDialog(
                    title = "Select Language",
                    selectedLanguage = userSelectedLanguage,
                    onLanguageSelected = { preferencesViewModel.changeLang(it); showSelectLangDialog = false },
                    onDismiss = { showSelectLangDialog = false }
                )
            }

            Text("Estamos en proceso...", style = MaterialTheme.typography.h6)

            Button(onClick = { showSelectLangDialog = true }) {

            }

            Divider()

            SaveAsJSONSection(visitsViewModel)
        }
    }
}

@Composable
private fun SaveAsJSONSection(visitsViewModel: VisitsViewModel) {
    Card(Modifier.fillMaxWidth(), elevation = 4.dp) {
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

        CenteredColumn(Modifier.padding(horizontal = 32.dp, vertical = 16.dp)) {
            Text(text = stringResource(R.string.save_json_section_title), style = MaterialTheme.typography.subtitle1)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { saverLauncher.launch("visits_${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}.json") }) {
                Text(text = "Save")
            }
        }
    }
}

