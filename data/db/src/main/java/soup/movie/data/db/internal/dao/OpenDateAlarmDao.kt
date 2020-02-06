package soup.movie.data.db.internal.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow
import soup.movie.data.db.entity.OpenDateAlarmEntity

@Dao
interface OpenDateAlarmDao {

    @Query("SELECT * FROM open_date_alarms")
    fun getAllFlow(): Flow<List<OpenDateAlarmEntity>>

    @Query("SELECT * FROM open_date_alarms WHERE open_date < :date")
    suspend fun getAllUntil(date: String): List<OpenDateAlarmEntity>

    @Query("SELECT COUNT(*) FROM open_date_alarms")
    suspend fun getCount(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insert(alarm: OpenDateAlarmEntity)

    @Update(onConflict = REPLACE)
    suspend fun updateAll(alarms: List<OpenDateAlarmEntity>)

    @Query("DELETE FROM open_date_alarms WHERE id = :movieId")
    suspend fun delete(movieId: String)

    @Query("DELETE FROM open_date_alarms where id in (:movieIdList)")
    suspend fun deleteAll(movieIdList: List<String>)
}
