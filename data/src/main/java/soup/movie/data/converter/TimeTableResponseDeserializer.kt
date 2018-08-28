package soup.movie.data.converter

import com.google.gson.*
import soup.movie.data.model.Day
import soup.movie.data.model.TimeTable
import soup.movie.data.response.TimeTableResponse
import java.lang.reflect.Type

class TimeTableResponseDeserializer : JsonDeserializer<TimeTableResponse> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): TimeTableResponse {
        val days = mutableListOf<Day>()
        if (json.isJsonArray) {
            json.asJsonArray.forEach {
                days.add(Gson().fromJson(it, Day::class.java))
            }
        }
        return TimeTableResponse(TimeTable(days))
    }
}
