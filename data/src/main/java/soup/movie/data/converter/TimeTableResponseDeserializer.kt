package soup.movie.data.converter

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

import java.lang.reflect.Type
import java.util.ArrayList

import soup.movie.data.model.Day
import soup.movie.data.model.TimeTable
import soup.movie.data.model.TimeTableResponse

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
