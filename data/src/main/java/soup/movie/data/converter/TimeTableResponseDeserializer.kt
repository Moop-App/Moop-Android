package soup.movie.data.converter

import com.google.gson.*
import soup.movie.data.model.Day
import soup.movie.data.model.TimeTable
import soup.movie.data.response.TimeTableResponse
import java.lang.reflect.Type
import java.util.*

class TimeTableResponseDeserializer : JsonDeserializer<TimeTableResponse> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): TimeTableResponse {
        val days = ArrayList<Day>()
        if (json.isJsonArray) {
            val gson = Gson()
            for (jsonElement in json.asJsonArray) {
                days.add(gson.fromJson(jsonElement, Day::class.java))
            }
        }
        return TimeTableResponse(TimeTable(days))
    }
}
