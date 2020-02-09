package soup.movie.data.db.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "open_date_alarms")
internal data class OpenDateAlarmEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val movieId: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "open_date")
    val openDate: String
)
