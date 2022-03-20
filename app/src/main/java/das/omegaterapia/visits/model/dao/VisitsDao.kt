package das.omegaterapia.visits.model.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import das.omegaterapia.visits.model.entities.Client
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitData

@Dao
interface VisitsDao {
    @Insert
    suspend fun addClient(client: Client)

    @Insert
    suspend fun addVisitData(visitData: VisitData)

    suspend fun addVisit(visitCard: VisitCard): Boolean{
        try {
            addClient(visitCard.client)
            addVisitData(visitCard.visitData)
            return true
        }
        catch (e: SQLiteConstraintException){
            return false
        }
    }

    @Transaction
    @Query("SELECT * FROM VisitData WHERE user = :currentUser")
    suspend fun getUserVisits(currentUser: String): List<VisitCard>
}