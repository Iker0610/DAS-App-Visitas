package das.omegaterapia.visits.activities.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.repositories.IVisitsRepository
import javax.inject.Inject

@HiltViewModel
class VisitsViewModel @Inject constructor(
    private val visitsRepository: IVisitsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // States
    val currentUser = savedStateHandle.get("LOGGED_USERNAME") as? String ?: ""
    val allVisits = visitsRepository.getUsersVisits(currentUser)
    val todaysVisits = visitsRepository.getUsersVisits(currentUser)


    // Events
    suspend fun addVisitCard(visitCard: VisitCard) = visitsRepository.addVisitCard(visitCard)

    // TODO
}