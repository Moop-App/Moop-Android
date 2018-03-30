package soup.movie.data.soup.converter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import soup.movie.data.soup.model.Day;
import soup.movie.data.soup.model.TimeTable;
import soup.movie.data.soup.model.TimeTableResponse;

public class TimeTableResponseDeserializer implements JsonDeserializer<TimeTableResponse> {

  @Override
  public TimeTableResponse deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {
    ArrayList<Day> days = new ArrayList<>();
    if (json.isJsonArray()) {
      Gson gson = new Gson();
      for (JsonElement jsonElement : json.getAsJsonArray()) {
        days.add(gson.fromJson(jsonElement, Day.class));
      }
    }
    TimeTableResponse response = new TimeTableResponse();
    response.setTimeTable(new TimeTable(days));
    return response;
  }
}
