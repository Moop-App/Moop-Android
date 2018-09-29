package soup.movie.data.model.converter

import com.google.gson.*
import soup.movie.data.model.Date
import soup.movie.data.model.TimeTable
import soup.movie.data.model.response.TimeTableResponse
import java.lang.reflect.Type

class TimeTableResponseDeserializer : JsonDeserializer<TimeTableResponse> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): TimeTableResponse {
        val dateList = mutableListOf<Date>()
        if (json.isJsonArray) {
            json.asJsonArray.forEach {
                dateList.add(Gson().fromJson(it, Date::class.java))
            }
        }
        return TimeTableResponse(TimeTable(dateList))
    }
}
