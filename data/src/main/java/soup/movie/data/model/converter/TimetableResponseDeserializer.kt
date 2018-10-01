package soup.movie.data.model.converter

import com.google.gson.*
import soup.movie.data.model.Date
import soup.movie.data.model.Timetable
import soup.movie.data.model.response.TimetableResponse
import java.lang.reflect.Type

class TimetableResponseDeserializer : JsonDeserializer<TimetableResponse> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): TimetableResponse {
        val dateList = mutableListOf<Date>()
        if (json.isJsonArray) {
            json.asJsonArray.forEach {
                dateList.add(Gson().fromJson(it, Date::class.java))
            }
        }
        return TimetableResponse(Timetable(dateList = dateList))
    }
}
