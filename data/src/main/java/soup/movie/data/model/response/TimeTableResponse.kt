package soup.movie.data.model.response

import com.google.gson.annotations.JsonAdapter
import soup.movie.data.model.TimeTable
import soup.movie.data.model.converter.TimeTableResponseDeserializer

@JsonAdapter(TimeTableResponseDeserializer::class)
data class TimeTableResponse(
        var timeTable: TimeTable)
