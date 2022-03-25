package das.omegaterapia.visits.activities.main.screens.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.preferences.IUserPreferences
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: IUserPreferences,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    init {
        Log.d("preferences", savedStateHandle.keys().joinToString(" - "))
    }
    val currentUser = savedStateHandle.get("username") as? String ?: "error"

}
