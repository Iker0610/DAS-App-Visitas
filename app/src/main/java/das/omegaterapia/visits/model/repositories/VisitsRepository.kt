package das.omegaterapia.visits.model.repositories

import android.database.sqlite.SQLiteConstraintException
import das.omegaterapia.visits.model.dao.VisitsDao
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface IVisitsRepository {
    fun getUsersVisits(username: String): Flow<List<VisitCard>>
    fun getUsersTodaysVisits(username: String): Flow<List<VisitCard>>
    fun getUsersVIPVisits(username: String): Flow<List<VisitCard>>
    suspend fun addVisitCard(visitCard: VisitCard): Boolean
    suspend fun addVisitCards(visitCards: List<VisitCard>): List<Boolean>
    suspend fun updateVisitCard(visitCard: VisitCard): Boolean
    suspend fun deleteVisitCard(visitId: VisitId)
}

class VisitsRepository @Inject constructor(private val visitsDao: VisitsDao) : IVisitsRepository {
    override fun getUsersVisits(username: String): Flow<List<VisitCard>> = visitsDao.getUserVisits(username)
    override fun getUsersTodaysVisits(username: String): Flow<List<VisitCard>> = visitsDao.getUserTodaysVisits(username)
    override fun getUsersVIPVisits(username: String): Flow<List<VisitCard>> = visitsDao.getUsersVIPVisits(username)

    override suspend fun addVisitCard(visitCard: VisitCard): Boolean = visitsDao.addVisitCard(visitCard)
    override suspend fun addVisitCards(visitCards: List<VisitCard>): List<Boolean> = visitsDao.addVisitCards(visitCards)
    override suspend fun updateVisitCard(visitCard: VisitCard): Boolean =
        try {
            visitsDao.updateVisitCard(visitCard)
            true
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            false
        }

    override suspend fun deleteVisitCard(visitId: VisitId) = visitsDao.deleteVisitCard(visitId)
}