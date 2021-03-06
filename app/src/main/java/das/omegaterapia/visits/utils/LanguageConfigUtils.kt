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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import das.omegaterapia.visits.R
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.theme.getButtonShape
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/*******************************************************************************
 ****                             Language Utils                            ****
 *******************************************************************************/

/**
 * Set of utils required for custom language management.
 *
 * Google does not support custom language (Locale) settings, and the solution is quite "hacky".
 */


/**
 * Get a ComponentActivity from the context given if possible, otherwise returns null.
 */
private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


/*************************************************
 **          App's Available Languages          **
 *************************************************/

/**
 * Class containing the App's available languages.
 *
 * @property language Full name of that language (in that language)
 * @property code Language's locale code
 */
enum class AppLanguage(val language: String, val code: String) {
    EN("English", "en"),
    ES("Espa??ol", "es");


    companion object {
        /**
         * Get the [AppLanguage] from a language code.
         *
         * @param code Language's code as string
         * @return That code's corresponding App's language as an [AppLanguage]. Defaults to [EN].
         */
        fun getFromCode(code: String) = when (code) {
            ES.code -> ES
            else -> EN
        }
    }
}


/*************************************************
 **            App's Language Manager           **
 *************************************************/

/**
 * Class to manage the current app's language.
 *
 * It is annotated with Hilt's singleton annotation so only one instance is created in the whole Application.
 */
@Singleton
class LanguageManager @Inject constructor() {

    // Current application's lang
    var currentLang: AppLanguage = AppLanguage.getFromCode(Locale.getDefault().language.lowercase())

    // Method to change the App's language setting a new locale
    fun changeLang(lang: AppLanguage, context: Context, recreate: Boolean = true) {

        // Check if there's any difference in language variables
        if (lang != currentLang || currentLang.code != Locale.getDefault().language) {

            // With the context create a new Locale and update configuration
            context.resources.apply {
                val locale = Locale(lang.code)
                val config = Configuration(configuration)

                context.createConfigurationContext(configuration)
                Locale.setDefault(locale)
                config.setLocale(locale)

                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, displayMetrics)
            }

            // Update current language
            currentLang = lang

            // If asked recreate the interface (this does not finish the activity)
            if (recreate) context.getActivity()?.recreate()
        }
    }
}


/*************************************************
 **         App's Language Picker Dialog        **
 *************************************************/

/**
 * Custom dialog with a scrollable list in middle that allows the user to pick one of the available languages.
 *
 * It follows Material Design's Confirmation Dialog design pattern, as stated in:
 * https://material.io/components/dialogs#confirmation-dialog
 *
 * @param title Dialog title.
 * @param selectedLanguage Current selected language.
 * @param onLanguageSelected Callback for onLanguageSelected event.
 * @param onDismiss Callback for dismiss event.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LanguagePickerDialog(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    title: String,
    onDismiss: () -> Unit,
) {
    /*------------------------------------------------
    |                     States                     |
    ------------------------------------------------*/

    var selected by rememberSaveable { mutableStateOf(selectedLanguage.code) }


    /*------------------------------------------------
    |                 User Interface                 |
    ------------------------------------------------*/
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RectangleShape,
            color = MaterialTheme.colors.surface,
            contentColor = contentColorFor(backgroundColor),
        ) {
            Column {

                //--------------   Dialog Title   --------------//
                Column(Modifier
                    .padding(horizontal = 24.dp)
                    .height(64.dp), verticalArrangement = Arrangement.Center) {
                    Text(text = title, style = MaterialTheme.typography.h6)
                }

                Divider()

                //---------   Dialog Content (List)   ----------//
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

                //-------------   Dialog Buttons   -------------//
                Divider()

                CenteredRow(Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.End) {
                    CenteredRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                        // Cancel TextButton
                        TextButton(onClick = onDismiss, shape = getButtonShape()) { Text(text = stringResource(R.string.cancel_button)) }

                        // Apply TextButton
                        TextButton(onClick = { onLanguageSelected(AppLanguage.getFromCode(selected)) }, shape = getButtonShape()) {
                            Text(text = stringResource(R.string.apply_button))
                        }
                    }
                }
            }
        }
    }
}