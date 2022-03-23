package das.omegaterapia.visits.model.dao

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import das.omegaterapia.visits.model.entities.Client
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitData
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitsDao {
    @Insert(onConflict = IGNORE)
    suspend fun addClient(client: Client)

    @Insert
    suspend fun addVisitData(visitData: VisitData)

    @Transaction
    suspend fun addVisitCard(visitCard: VisitCard): Boolean {
        return try {
            try {
                addClient(visitCard.client)
            } catch (e: Exception) {
                Log.e("ROOM-Insert Client from VisitCard", "${visitCard.client} ${visitCard.client.phoneNum} ${visitCard.visitData.mainClientPhone}")
                e.printStackTrace()
            }
            addVisitData(visitCard.visitData)
            true
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addVisitCards(visitCards: List<VisitCard>): List<Boolean> = visitCards.map { addVisitCard(it) }

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