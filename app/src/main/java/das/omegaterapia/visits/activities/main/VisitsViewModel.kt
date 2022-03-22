package das.omegaterapia.visits.activities.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.repositories.IVisitsRepository
import das.omegaterapia.visits.utils.TemporalConverter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class VisitsViewModel @Inject constructor(
    private val visitsRepository: IVisitsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

//    init {
//        viewModelScope.launch {
//            val user = savedStateHandle.get("LOGGED_USERNAME") as? String ?: ""
//            visitsRepository.addVisitCards(visitList.map { it.user = user; it.visitData.mainClientPhone = it.client.phoneNum; return@map it })
//        }
//    }

    // States
    val currentUser = savedStateHandle.get("LOGGED_USERNAME") as? String ?: ""
    val allVisits = visitsRepository.getUsersVisits(currentUser)
        .map { visitList -> TemporalConverter.WEEK.groupDates(visitList, key = VisitCard::visitDate::get) }

    val todaysVisits = visitsRepository.getUsersTodaysVisits(currentUser)
        .map { visitList -> TemporalConverter.HOUR_WITH_DAY.groupDates(visitList, key = VisitCard::visitDate::get) }


    // Events
    suspend fun addVisitCard(visitCard: VisitCard) = visitsRepository.addVisitCard(visitCard.also { it.user = currentUser })

    // TODO
}