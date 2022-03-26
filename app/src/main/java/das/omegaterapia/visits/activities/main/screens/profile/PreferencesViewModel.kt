package das.omegaterapia.visits.activities.main.screens.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.preferences.IUserPreferences
import das.omegaterapia.visits.utils.AppLanguage
import das.omegaterapia.visits.utils.LanguageManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: IUserPreferences,
    private val languageManager: LanguageManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val currentUser = (savedStateHandle.get("username") as? String ?: savedStateHandle.get("LOGGED_USERNAME") as? String)!!
    // lateinit var context: Context

    val currentSetLang by languageManager::currentLang
    val currentPrefLang = preferencesRepository.userLanguage(currentUser).map { AppLanguage.getFromCode(it) }

    // Language related
    fun changeLang(newLang: AppLanguage, context: Context) {
        languageManager.changeLang(newLang, context)
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.setUserLanguage(currentUser, newLang.code) }
    }

    fun reloadLang(lang: AppLanguage, context: Context) = languageManager.changeLang(lang, context, false)
}
