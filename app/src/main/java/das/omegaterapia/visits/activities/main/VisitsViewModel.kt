package das.omegaterapia.visits.activities.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.model.repositories.VisitsRepository
import javax.inject.Inject

@HiltViewModel
class VisitsViewModel @Inject constructor(
    private val visitsRepository: VisitsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    init {
        val currentUser = savedStateHandle.get("LOGGED_USERNAME") as? String ?: ""
    }
    // TODO
}