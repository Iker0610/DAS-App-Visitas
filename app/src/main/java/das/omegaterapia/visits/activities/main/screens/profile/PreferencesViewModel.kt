package das.omegaterapia.visits.activities.main.screens.profile

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
    init {
        Log.d("preferences", savedStateHandle.keys().joinToString(" - "))
    }

    val currentUser = (savedStateHandle.get("username") as? String ?: savedStateHandle.get("LOGGED_USERNAME") as? String)!!

    val currentPrefLang = preferencesRepository.userLanguage(currentUser)
        .map { AppLanguage.getFromCode(it) }
        .onEach { Log.d("lang", "lang changed: ${it.code}");languageManager.changeLang(it) }

    val currentSetLang by languageManager::currentLang

    // Language related
    fun changeLang(newLang: AppLanguage) {
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.setUserLanguage(currentUser, newLang.code) }
    }

    fun reloadLang() = languageManager.reloadLang()
}
