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
        return TimetableResponse(Timetable(
                dateList = json.takeIf { it.isJsonArray }
                        ?.asJsonArray
                        ?.toList()
                        ?.map { Gson().fromJson(it, Date::class.java) }
                        ?.filter { it?.hallList?.isNotEmpty() ?: false }
                        ?: emptyList()))
    }
}
