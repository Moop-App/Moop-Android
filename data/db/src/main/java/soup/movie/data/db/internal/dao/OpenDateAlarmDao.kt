package soup.movie.data.db.internal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import soup.movie.data.db.internal.entity.OpenDateAlarmEntity

@Dao
internal interface OpenDateAlarmDao {

    @Query("SELECT * FROM open_date_alarms")
    fun getAllFlow(): Flow<List<OpenDateAlarmEntity>>

    @Query("SELECT * FROM open_date_alarms WHERE open_date < :date")
    suspend fun getAllUntil(date: String): List<OpenDateAlarmEntity>

    @Query("SELECT COUNT(*) FROM open_date_alarms")
    suspend fun getCount(): Int

    suspend fun hasAlarms(): Boolean {
        return getCount() > 0
    }

    @Insert(onConflict = REPLACE)
    suspend fun insert(alarm: OpenDateAlarmEntity)

    @Update(onConflict = REPLACE)
    suspend fun updateAll(alarms: List<OpenDateAlarmEntity>)

    @Query("DELETE FROM open_date_alarms WHERE id = :movieId")
    suspend fun delete(movieId: String)

    @Query("DELETE FROM open_date_alarms where id in (:movieIdList)")
    suspend fun deleteAll(movieIdList: List<String>)
}
