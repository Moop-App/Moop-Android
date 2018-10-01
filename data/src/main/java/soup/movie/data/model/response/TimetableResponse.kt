package soup.movie.data.model.response

import com.google.gson.annotations.JsonAdapter
import soup.movie.data.model.Timetable
import soup.movie.data.model.converter.TimetableResponseDeserializer

@JsonAdapter(TimetableResponseDeserializer::class)
data class TimetableResponse(var timetable: Timetable)
