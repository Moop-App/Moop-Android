/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package soup.movie.data.database.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import soup.movie.data.database.impl.entity.OpenDateAlarmEntity

@Dao
interface OpenDateAlarmDao {

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
