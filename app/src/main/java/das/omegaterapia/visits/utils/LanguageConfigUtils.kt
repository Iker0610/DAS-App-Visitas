package das.omegaterapia.visits.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import das.omegaterapia.visits.ui.components.generic.CenteredRow
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

                @Suppress("DEPRECATION")
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
    title: String,
    onDismiss: () -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(selectedLanguage.code) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RectangleShape,
            color = MaterialTheme.colors.surface,
            contentColor = contentColorFor(backgroundColor),
        ) {
            Column {
                Column(Modifier
                    .padding(horizontal = 24.dp)
                    .height(64.dp), verticalArrangement = Arrangement.Center) {
                    Text(text = title, style = MaterialTheme.typography.h6)
                }

                Divider()

                Column(Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                ) {
                    AppLanguage.values().forEach { lang ->
                        ListItem(
                            modifier = Modifier.clickable { selected = lang.code },
                            trailing = { Checkbox(checked = selected == lang.code, onCheckedChange = { selected = lang.code }) },
                            text = { Text(text = lang.language, style = MaterialTheme.typography.body1) }
                        )
                    }
                }

                Divider()

                CenteredRow(Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.End) {
                    CenteredRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onDismiss) { Text(text = "CANCEL") }
                        TextButton(onClick = { onLanguageSelected(AppLanguage.getFromCode(selected)) }) { Text(text = "APPLY") }
                    }
                }
            }
        }
    }
}