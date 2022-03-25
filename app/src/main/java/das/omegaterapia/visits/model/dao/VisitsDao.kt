package das.omegaterapia.visits.model.dao

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import das.omegaterapia.visits.model.entities.Client
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitData
import das.omegaterapia.visits.model.entities.VisitId
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitsDao {
    @Insert
    suspend fun addClient(client: Client)

    @Insert
    suspend fun addVisitData(visitData: VisitData)

    @Transaction
    suspend fun addVisitCard(visitCard: VisitCard): Boolean {
        return try {
            try {
                addClient(visitCard.client)
            } catch (e: Exception) {
                updateClientData(visitCard.client)
            }
            addVisitData(visitCard.visitData)
            true
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addVisitCards(visitCards: List<VisitCard>): List<Boolean> = visitCards.map { addVisitCard(it) }

    @Update
    suspend fun updateVisitData(visitData: VisitData)

    @Update
    fun updateClientData(client: Client): Int

    @Transaction
    suspend fun updateVisitCard(visitCard: VisitCard) {

        if (0 == updateClientData(visitCard.client)) {
            addClient(visitCard.client)
        }
        updateVisitData(visitCard.visitData)
    }

    @Delete(entity = VisitData::class)
    suspend fun deleteVisitCard(visitId: VisitId)


    @Transaction
    @Query("SELECT * FROM VisitData WHERE user = :currentUser ORDER BY visit_date")
    fun getUserVisits(currentUser: String): Flow<List<VisitCard>>

    @Transaction
    @Query("SELECT * FROM VisitData WHERE user = :currentUser AND DATE(visit_date, 'unixepoch') = DATE('now') ORDER BY visit_date")
    fun getUserTodaysVisits(currentUser: String): Flow<List<VisitCard>>

    @Transaction
    @Query("SELECT * FROM VisitData WHERE user = :currentUser AND is_VIP = 1 ORDER BY visit_date")
    fun getUsersVIPVisits(currentUser: String): Flow<List<VisitCard>>


}