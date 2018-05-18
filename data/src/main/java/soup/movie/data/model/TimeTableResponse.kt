package soup.movie.data.model

import com.google.gson.annotations.JsonAdapter

import soup.movie.data.converter.TimeTableResponseDeserializer

@JsonAdapter(TimeTableResponseDeserializer::class)
data class TimeTableResponse(var timeTable: TimeTable)