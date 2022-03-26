package das.omegaterapia.visits.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dagger.hilt.android.qualifiers.ApplicationContext
import das.omegaterapia.visits.OmegaterapiaVisitsApp
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


enum class AppLanguage(val language: String, val code: String) {
    EN("English", "en"),
    ES("Español", "es");

    companion object {
        fun getFromCode(code: String) = when (code) {
            ES.code -> ES
            else -> EN
        }
    }
}


@Singleton
class LanguageManager @Inject constructor() {
    var currentLang: AppLanguage = AppLanguage.getFromCode(Locale.getDefault().language.lowercase())

    fun changeLang(lang: AppLanguage, context: Context, recreate: Boolean = true) {
        if (lang != currentLang || currentLang.code != Locale.getDefault().language) {
            currentLang = lang

            context.resources.apply {
                val locale = Locale(lang.code)
                val config = Configuration(configuration)

                context.createConfigurationContext(configuration)
                Locale.setDefault(locale)
                config.setLocale(locale)
                context.resources.updateConfiguration(config, displayMetrics)
            }
            if (recreate) context.getActivity()?.recreate()
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LanguagePickerDialog(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    title: String = "SelectLanguage",
    onDismiss: () -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(selectedLanguage.code) }

    AlertDialog(
        title = { Text(text = title) },
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = { onLanguageSelected(AppLanguage.getFromCode(selected)) }) { Text(text = "APPLY") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text = "CANCEL") } },
        text = {
            Divider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                AppLanguage.values().forEach { lang ->
                    ListItem(
                        modifier = Modifier.clickable { selected = lang.code },
                        trailing = { Checkbox(checked = selected == lang.code, onCheckedChange = { selected = lang.code }) },
                        text = { Text(text = lang.language) }
                    )
                }
            }
            Divider()
        }
    )
}