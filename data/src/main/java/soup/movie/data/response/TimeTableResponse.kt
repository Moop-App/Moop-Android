package soup.movie.data.response

import com.google.gson.annotations.JsonAdapter

import soup.movie.data.converter.TimeTableResponseDeserializer
import soup.movie.data.model.TimeTable

@JsonAdapter(TimeTableResponseDeserializer::class)
data class TimeTableResponse(
        var timeTable: TimeTable)
