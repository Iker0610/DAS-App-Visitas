package das.omegaterapia.visits.model.repositories

import das.omegaterapia.visits.model.dao.VisitsDao
import das.omegaterapia.visits.model.entities.VisitCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


interface IVisitsRepository {
    fun getUsersVisits(username: String): Flow<List<VisitCard>>
    fun getUsersTodaysVisits(username: String): Flow<List<VisitCard>>
    suspend fun addVisitCard(visitCard: VisitCard): Boolean
    suspend fun addVisitCards(visitCards: List<VisitCard>): List<Boolean>
}

class VisitsRepository @Inject constructor(private val visitsDao: VisitsDao) : IVisitsRepository {
    override fun getUsersVisits(username: String): Flow<List<VisitCard>> = visitsDao.getUserVisits(username)
    override fun getUsersTodaysVisits(username: String): Flow<List<VisitCard>> = visitsDao.getUserTodaysVisits(username)
    override suspend fun addVisitCard(visitCard: VisitCard): Boolean = visitsDao.addVisitCard(visitCard)
    override suspend fun addVisitCards(visitCards: List<VisitCard>): List<Boolean> = visitsDao.addVisitCards(visitCards)
}