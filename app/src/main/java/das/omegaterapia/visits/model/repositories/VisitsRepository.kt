package das.omegaterapia.visits.model.repositories

import das.omegaterapia.visits.model.dao.VisitsDao
import das.omegaterapia.visits.model.entities.VisitCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


interface IVisitsRepository

class VisitsRepository @Inject constructor(private val visitsDao: VisitsDao) : IVisitsRepository {
    fun getUsersTodaysVisits(username: String): Flow<List<VisitCard>> = visitsDao.getUserVisits(username)
}